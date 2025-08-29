package br.com.bogan.resolve.injection;

import br.com.bogan.resolve.ResolverContext;
import br.com.bogan.definition.InjectionPoint;

public interface SpecialInjectionAdapter {

    boolean supports(InjectionPoint p);

    Object resolve(InjectionPoint p, ResolverContext ctx);

    int getOrder();
}
