package br.com.bogan.registry;

import br.com.bogan.env.BoganEnvironment;

/**
 * Runs after definitions are registered but before the factory is initialized.
 */
public interface BoganDefinitionPostProcessor {
    void postProcessDefinitions(DefinitionRegistry registry, BoganEnvironment environment);
}
