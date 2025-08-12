package br.com.bogan.scan;

import br.com.bogan.annotations.*;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.definition.DependencyRequirement;
import br.com.bogan.definition.InjectionMode;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class ComponentSniffer {

    public Collection<ComponentDefinition> discover(Class<?> primaryClass) {
        String basePkg = primaryClass.getPackage().getName();
        ClassLoader cl = primaryClass.getClassLoader();

        try (ScanResult scan = new ClassGraph()
                .enableAllInfo()
                .overrideClassLoaders(cl)
                .acceptPackages(basePkg)
                .scan()) {

            Class<?> scanPoint = findSingleScanPoint(scan);
            String root = scanPoint.getPackage().getName();

            try (ScanResult scan2 = new ClassGraph()
                    .enableAllInfo()
                    .overrideClassLoaders(cl)      // <-- idem
                    .acceptPackages(root)
                    .scan()) {
                return buildDefinitions(scan2, root);
            }
        }
    }

    private Class<?> findSingleScanPoint(ScanResult scan) {
        List<Class<?>> found = scan.getClassesWithAnnotation(ScanPoint.class.getName()).loadClasses();
        if (found.size() != 1)
            throw new IllegalStateException("Exactly one @ScanPoint is required under primary package; found " + found.size());
        return found.getFirst();
    }

    private Collection<ComponentDefinition> buildDefinitions(ScanResult scan, String rootPackage) {
        Collection<ComponentDefinition> out = new ArrayList<>();

        for (ClassInfo ci : scan.getClassesWithAnnotation(Component.class.getName())) {
            if (!ci.getPackageName().startsWith(rootPackage)) continue;

            Class<?> clazz = ci.loadClass();
            Component comp = clazz.getAnnotation(Component.class);
            String explicitName = comp.value();
            String name = explicitName.isBlank() ? decapitalize(clazz.getSimpleName()) : explicitName;
            ScopeType scope = comp.scope();
            boolean lazy = comp.lazy();

            Set<String> qualifiers = new HashSet<>();
            if (clazz.isAnnotationPresent(Qualifier.class))
                qualifiers.add(clazz.getAnnotation(Qualifier.class).value());

            InjectionMode mode = pickInjectionMode(clazz);
            List<DependencyRequirement> deps = extractConstructorRequirements(clazz);

            ComponentDefinition def = new ComponentDefinition(name, clazz, scope, lazy, mode, qualifiers, deps, 0);
            out.add(def);
        }

        return out;
    }

    private InjectionMode pickInjectionMode(Class<?> clazz) {
        boolean anyInjectCtor = Arrays.stream(clazz.getDeclaredConstructors()).anyMatch(c -> c.isAnnotationPresent(Inject.class));
        boolean hasArgsCtor = Arrays.stream(clazz.getDeclaredConstructors()).anyMatch(c -> c.getParameterCount() > 0);
        boolean anyInjectField = Arrays.stream(clazz.getDeclaredFields()).anyMatch(f -> f.isAnnotationPresent(Inject.class));

        if ((anyInjectCtor || hasArgsCtor) && anyInjectField) return InjectionMode.MIXED;
        if (anyInjectCtor || hasArgsCtor) return InjectionMode.CONSTRUCTOR;
        if (anyInjectField) return InjectionMode.FIELD;

        return InjectionMode.CONSTRUCTOR;
    }

    private List<DependencyRequirement> extractConstructorRequirements(Class<?> clazz) {
        Constructor<?> chosen = chooseCtorForMetadata(clazz);
        if (chosen == null || chosen.getParameterCount() == 0) return List.of();

        List<DependencyRequirement> reqs = new ArrayList<>();
        var params = chosen.getParameters();

        for (int i = 0; i < params.length; i++) {
            var p = params[i];
            String qualifier = p.isAnnotationPresent(Qualifier.class) ? p.getAnnotation(Qualifier.class).value() : null;
            reqs.add(new DependencyRequirement(p.getType(), qualifier, true, "constructor:param#" + i));
        }

        return reqs;
    }

    private Constructor<?> chooseCtorForMetadata(Class<?> clazz) {
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        Optional<Constructor<?>> inj = Arrays.stream(ctors).filter(c -> c.isAnnotationPresent(Inject.class)).findFirst();
        if (inj.isPresent()) return inj.get();
        return Arrays.stream(ctors).filter(c -> c.getParameterCount() > 0).findFirst().orElseGet(() -> {
            try {
                return clazz.getDeclaredConstructor();
            } catch (NoSuchMethodException e) {
                return null;
            }
        });
    }

    @SuppressWarnings("unused")
    private List<Field> extractInjectFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Field f : clazz.getDeclaredFields()) if (f.isAnnotationPresent(Inject.class)) fields.add(f);
        return fields;
    }

    private String decapitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}
