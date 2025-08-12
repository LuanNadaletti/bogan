package br.com.bogan.instantiation;

import br.com.bogan.annotations.Inject;
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
        return Arrays.stream(ctors)
                .filter(c -> c.isAnnotationPresent(Inject.class) && c.getParameterCount() == arity)
                .findFirst()
                .orElseGet(() -> Arrays.stream(ctors)
                        .filter(c -> c.getParameterCount() == arity)
                        .findFirst()
                        .orElseGet(() -> {
                            try {
                                return clazz.getDeclaredConstructor();
                            } catch (NoSuchMethodException e) {
                                throw new IllegalStateException("No suitable constructor found for " + clazz.getName());
                            }
                        }));
    }
}
