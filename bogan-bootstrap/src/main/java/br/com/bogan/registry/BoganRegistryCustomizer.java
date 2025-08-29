package br.com.bogan.registry;


import br.com.bogan.env.BoganEnvironment;
import br.com.bogan.util.Ordered;

/**
 * Runs before definitions are registered (pre-registration customization).
 */
public interface BoganRegistryCustomizer extends Ordered {

    void customize(DefinitionRegistry registry, BoganEnvironment environment);
}
