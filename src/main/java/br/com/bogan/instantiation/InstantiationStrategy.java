package br.com.bogan.instantiation;

import br.com.bogan.definition.ComponentDefinition;

public interface InstantiationStrategy {

    Object instantiate(ComponentDefinition def, Object[] resolvedConstructorArgs) throws Exception;
}
