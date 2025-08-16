package br.com.bogan.resolve.injection;

import br.com.bogan.definition.InjectionPoint;
import br.com.bogan.error.NoSuchComponentException;
import br.com.bogan.factory.ComponentFactory;
import br.com.bogan.provider.Provider;
import br.com.bogan.resolve.ResolverContext;
import br.com.bogan.util.ComponentNameUtils;

public class ProviderInjectionAdapter implements SpecialInjectionAdapter {

    @Override
    public boolean supports(InjectionPoint p) {
        return p.getRawType().isAssignableFrom(Provider.class);
    }

    @Override
    public Object resolve(InjectionPoint p, ResolverContext ctx) {
        final ComponentFactory factory = ctx.factory();
        final Class<?> type = p.getTypeArgument();
        final String qualifier = p.getQualifier();
        final boolean required = p.isRequired();

        return (Provider<Object>) () -> {
            Object bean = factory.getComponent(type, qualifier, ctx);
            if (bean == null && required) {
                throw new NoSuchComponentException(
                        ComponentNameUtils.formatNameAndQualifier(type, qualifier)
                );
            }
            return bean;
        };
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
