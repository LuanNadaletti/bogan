package br.com.bogan.factory;

import br.com.bogan.env.BoganEnvironment;
import br.com.bogan.util.Ordered;

/**
 * Runs after the ComponentFactory is created and before initialize().
 */
public interface BoganFactoryPostProcessor extends Ordered {

    void postProcessFactory(ComponentFactory factory, BoganEnvironment environment);
}
