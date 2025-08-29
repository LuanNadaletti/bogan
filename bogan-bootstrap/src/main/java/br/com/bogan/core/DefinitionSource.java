package br.com.bogan.core;

import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.env.BoganEnvironment;
import br.com.bogan.util.Ordered;

import java.util.Collection;

/**
 * Source of component definitions (e.g., annotation scanning, file-based, remote).
 */
public interface DefinitionSource extends Ordered {

    Collection<ComponentDefinition> load(Class<?> primaryClass, BoganEnvironment environment);
}
