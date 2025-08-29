package br.com.bogan.util;

import br.com.bogan.annotations.PreDestroy;
import br.com.bogan.error.CreationException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReflectionUtil {

    public static Optional<Constructor<?>> chooseConstructor(Class<?> clazz, Constructor<?>[] ctors, Object[] args) {
        int arity = (args == null) ? 0 : args.length;

        if (ctors.length == 1) {
            Constructor<?> only = ctors[0];
            if (only.getParameterCount() != arity)
                throw new IllegalStateException("Single constructor arity mismatch for " + clazz.getName());
            return Optional.of(only);
        }

        return Arrays.stream(ctors)
                .filter(c -> c.isAnnotationPresent(br.com.bogan.annotations.Inject.class))
                .filter(c -> c.getParameterCount() == arity)
                .findFirst();
    }

    public static void executeMethod(Method method, Object instance, Object... args) {
        try {
            method.setAccessible(true);
            method.invoke(instance, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new CreationException(instance.getClass().getName() + ".@PostConstruct", e);
        }
    }

    public static Optional<Method> getPreDestroyMethod(Class<?> clazz) {
        List<Method> preDestroyMethods = getMethodsWithAnnotation(clazz, PreDestroy.class);

        if (preDestroyMethods.size() > 1)
            throw new IllegalArgumentException("More than one PreDestroy method found for " + clazz.getName());

        if (preDestroyMethods.isEmpty())
            return Optional.empty();

        return Optional.of(preDestroyMethods.getFirst());
    }

    public static List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotation))
                .toList();
    }
}
