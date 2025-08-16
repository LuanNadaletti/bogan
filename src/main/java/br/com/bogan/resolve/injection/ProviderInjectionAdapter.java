package br.com.bogan.resolve.injection;

import br.com.bogan.error.NoSuchComponentException;
import br.com.bogan.provider.Provider;
import br.com.bogan.resolve.ResolverContext;
import br.com.bogan.definition.InjectionPoint;

public class ProviderInjectionAdapter implements SpecialInjectionAdapter {

    @Override
    public boolean supports(InjectionPoint p) {
        return p.getRawType().isAssignableFrom(Provider.class);
    }

    @Override
    public Object resolve(InjectionPoint p, ResolverContext ctx) {
        Object candidate = ctx.factory().getComponent(p.getTypeArgument(), p.getQualifier(), ctx);

        if (p.isRequired() && candidate == null) {
            throw new NoSuchComponentException(p.getMemberName());
        }

        return (Provider<Object>) () -> candidate;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
