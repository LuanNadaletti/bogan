package br.com.bogan.util;

import java.lang.reflect.Constructor;
import java.util.Arrays;
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
}
