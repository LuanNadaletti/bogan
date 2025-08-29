package br.com.bogan.resolve;

import br.com.bogan.definition.ComponentDefinition;

public interface DependencyResolver {

    Object[] resolveConstructorArgs(ComponentDefinition def, ResolverContext ctx);
    void injectFields(Object target, ComponentDefinition def, ResolverContext ctx);
}
