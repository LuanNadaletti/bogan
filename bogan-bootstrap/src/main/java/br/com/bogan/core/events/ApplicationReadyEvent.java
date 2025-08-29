package br.com.bogan.core.events;

import br.com.bogan.env.BoganEnvironment;
import br.com.bogan.factory.ComponentFactory;

public final class ApplicationReadyEvent extends BoganEvent {

    private final ComponentFactory factory;

    public ApplicationReadyEvent(Class<?> primaryClass, BoganEnvironment env, ComponentFactory factory) {
        super(primaryClass, env);
        this.factory = factory;
    }

    public ComponentFactory getFactory() {
        return factory;
    }
}
