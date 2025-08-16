package br.com.bogan.instantiation;

import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.util.ReflectionUtil;

import java.lang.reflect.Constructor;

public class ConstructorInstantiationStrategy implements InstantiationStrategy {

    @Override
    public Object instantiate(ComponentDefinition def, Object[] args) throws Exception {
        Class<?> clazz = def.getComponentClass();
        Constructor<?>[] ctors = clazz.getDeclaredConstructors();
        Constructor<?> chosen = ReflectionUtil.chooseConstructor(clazz, ctors, args)
                .orElseThrow(() -> {
                    int arity = (args == null) ? 0 : args.length;
                    return new IllegalStateException("Multiple constructors in " + clazz.getName()
                            + " require @Inject on the chosen one (arity=" + arity + ")");
                });

        chosen.setAccessible(true);
        return chosen.newInstance(args);
    }
}
