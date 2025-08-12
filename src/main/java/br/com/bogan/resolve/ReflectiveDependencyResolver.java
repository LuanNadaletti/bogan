package br.com.bogan.resolve;

import br.com.bogan.annotations.Inject;
import br.com.bogan.annotations.Qualifier;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.definition.InjectionMode;
import br.com.bogan.error.InjectionException;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ReflectiveDependencyResolver implements DependencyResolver {

    @Override
    public Object[] resolveConstructorArgs(ComponentDefinition def, ResolverContext ctx) {
        if (def.getInjectionMode() == InjectionMode.FIELD) return new Object[0];
        var reqs = def.getDependencies();
        var args = new ArrayList<>(reqs.size());
        for (var r : reqs) {
            Object dep = ctx.factory().getComponent(r.getType(), r.getQualifier(), ctx);
            args.add(dep);
        }
        return args.toArray();
    }

    @Override
    public void injectFields(Object target, ComponentDefinition def, ResolverContext ctx) {
        if (def.getInjectionMode() == InjectionMode.CONSTRUCTOR) return;
        for (Field f : def.getComponentClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Inject.class)) {
                String q = f.isAnnotationPresent(Qualifier.class) ? f.getAnnotation(Qualifier.class).value() : null;
                Object dep = ctx.factory().getComponent(f.getType(), q, ctx);
                try {
                    f.setAccessible(true);
                    f.set(target, dep);
                } catch (IllegalAccessException e) {
                    throw new InjectionException("field:" + f.getName(), e);
                }
            }
        }
    }
}
