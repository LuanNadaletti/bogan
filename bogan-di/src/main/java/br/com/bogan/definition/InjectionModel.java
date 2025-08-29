package br.com.bogan.definition;

import br.com.bogan.annotations.Inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Precomputed reflective model for dependency injection of a given component class.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Select a deterministic constructor to instantiate the component.</li>
 *   <li>Expose constructor parameters as {@link InjectionPoint}s in declaration order.</li>
 *   <li>Expose field injection points (@Inject fields) as {@link InjectionPoint}s.</li>
 * </ul>
 * <p>
 * Selection rule (Spring-like):
 * <ol>
 *   <li>If there is exactly one constructor, use it.</li>
 *   <li>Else, if there is exactly one constructor annotated with {@code @Inject}, use it.</li>
 *   <li>Else, if a no-arg constructor exists, use it.</li>
 *   <li>Else, fail with a clear error asking for {@code @Inject} on the intended constructor.</li>
 * </ol>
 */
public final class InjectionModel {

    private final Constructor<?> ctor;
    private final List<InjectionPoint> ctorParams;
    private final List<InjectionPoint> injectionPoints; // fields

    public InjectionModel(Class<?> clazz) {
        this.ctor = selectConstructor(clazz);
        this.ctorParams = buildCtorParams(this.ctor);
        this.injectionPoints = buildFieldPoints(clazz);
    }

    private static Constructor<?> selectConstructor(Class<?> clazz) {
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();

        // (1) Exactly one constructor
        if (ctors.length == 1) {
            Constructor<?> only = ctors[0];
            only.setAccessible(true);
            return only;
        }

        // (2) Exactly one @Inject constructor
        List<Constructor<?>> annotated = Arrays.stream(ctors)
                .filter(c -> c.isAnnotationPresent(Inject.class))
                .toList();
        if (annotated.size() == 1) {
            Constructor<?> chosen = annotated.get(0);
            chosen.setAccessible(true);
            return chosen;
        }
        if (annotated.size() > 1) {
            throw new IllegalStateException("Multiple constructors annotated with @Inject in " + clazz.getName());
        }

        // (3) Fallback to no-arg constructor if present
        try {
            Constructor<?> noArg = clazz.getDeclaredConstructor();
            noArg.setAccessible(true);
            return noArg;
        } catch (NoSuchMethodException ignore) {
            // continue to (4)
        }

        // (4) No rule matched -> require @Inject to disambiguate
        throw new IllegalStateException(
                "Multiple constructors in " + clazz.getName() +
                        " and none marked with @Inject; please annotate the intended constructor."
        );
    }

    private static List<InjectionPoint> buildCtorParams(Constructor<?> ctor) {
        Parameter[] params = ctor.getParameters();
        if (params.length == 0) return List.of();

        List<InjectionPoint> points = new ArrayList<>(params.length);
        for (Parameter p : params) {
            points.add(InjectionPoint.from(p));
        }
        return List.copyOf(points);
    }

    private static List<InjectionPoint> buildFieldPoints(Class<?> clazz) {
        List<InjectionPoint> points = new ArrayList<>();
        Class<?> current = clazz;

        // Walk class hierarchy to allow field injection in superclasses as well
        while (current != null && current != Object.class) {
            for (Field f : current.getDeclaredFields()) {
                if (f.isAnnotationPresent(Inject.class)) {
                    points.add(InjectionPoint.from(f));
                }
            }
            current = current.getSuperclass();
        }

        return List.copyOf(points);
    }

    public Constructor<?> getCtor() {
        return ctor;
    }

    public List<InjectionPoint> getCtorParams() {
        return ctorParams;
    }

    public List<InjectionPoint> getInjectionPoints() {
        return injectionPoints;
    }
}
