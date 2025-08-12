package br.com.bogan.resolve;

import br.com.bogan.annotations.Inject;
import br.com.bogan.annotations.Qualifier;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.definition.InjectionMode;
import br.com.bogan.error.AmbiguousDependencyException;
import br.com.bogan.error.InjectionException;
import br.com.bogan.error.NoSuchComponentException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveDependencyResolver implements DependencyResolver {

    @Override
    public Object[] resolveConstructorArgs(ComponentDefinition def, ResolverContext ctx) {
        if (def.getInjectionMode() == InjectionMode.FIELD) return new Object[0];
        var reqs = def.getDependencies();
        var args = new ArrayList<>(reqs.size());
        for (var r : reqs) {
            args.add(resolveByTypeAndQualifier(ctx, r.getType(), r.getQualifier(), r.isRequired()));
        }
        return args.toArray();
    }

    @Override
    public void injectFields(Object target, ComponentDefinition def, ResolverContext ctx) {
        if (def.getInjectionMode() == InjectionMode.CONSTRUCTOR) return;
        for (Field f : def.getComponentClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Inject.class)) {
                String q = f.isAnnotationPresent(Qualifier.class) ? f.getAnnotation(Qualifier.class).value() : null;
                Object dep = resolveByTypeAndQualifier(ctx, f.getType(), q, true);
                try {
                    f.setAccessible(true);
                    f.set(target, dep);
                } catch (IllegalAccessException e) {
                    throw new InjectionException("field:" + f.getName(), e);
                }
            }
        }
    }

    private Object resolveByTypeAndQualifier(ResolverContext ctx, Class<?> type, String qualifier, boolean required) {
        if (type == java.util.Optional.class) {
            throw new UnsupportedOperationException("Handle Optional<T> at parameter metadata time");
        }

        var candidates = ctx.factory().definitionsByType(type);
        if (candidates.isEmpty()) {
            if (required) throw new NoSuchComponentException(type.getName());
            return null;
        }
        if (qualifier != null) {
            candidates = candidates.stream()
                    .filter(d -> d.getQualifiers().contains(qualifier))
                    .toList();
            if (candidates.isEmpty())
                throw new NoSuchComponentException(type.getName() + " (qualifier=" + qualifier + ")");
        }
        if (candidates.size() > 1) {
            var primary = candidates.stream()
                    .filter(d -> d.getComponentClass().isAnnotationPresent(br.com.bogan.annotations.Primary.class))
                    .findFirst();
            if (primary.isPresent()) candidates = List.of(primary.get());
        }
        if (candidates.size() > 1) {
            throw new AmbiguousDependencyException(type, candidates.stream().map(ComponentDefinition::getName).toList());
        }
        var def = candidates.getFirst();
        return ctx.factory().getByDefinition(def, ctx);
    }
}
