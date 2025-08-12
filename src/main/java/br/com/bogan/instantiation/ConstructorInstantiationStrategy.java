package br.com.bogan.instantiation;

import br.com.bogan.definition.ComponentDefinition;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(ComponentDefinition def, Object[] args) throws Exception {
        Class<?> clazz = def.getComponentClass();
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        Constructor<?> chosen = chooseConstructor(clazz, ctors, args);
        chosen.setAccessible(true);
        return chosen.newInstance(args);
    }

    private Constructor<?> chooseConstructor(Class<?> clazz, Constructor<?>[] ctors, Object[] args) {
        int arity = (args == null) ? 0 : args.length;

        if (ctors.length == 1) {
            Constructor<?> only = ctors[0];
            if (only.getParameterCount() != arity) {
                throw new IllegalStateException("Single constructor arity mismatch for " + clazz.getName());
            }
            return only;
        }

        var annotated = Arrays.stream(ctors)
                .filter(c -> c.isAnnotationPresent(br.com.bogan.annotations.Inject.class))
                .filter(c -> c.getParameterCount() == arity)
                .findFirst();

        if (annotated.isPresent()) return annotated.get();

        throw new IllegalStateException("Multiple constructors in " + clazz.getName()
                + " require @Inject on the chosen one (arity=" + arity + ")");
    }
}
