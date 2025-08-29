package br.com.bogan.core.events;

import br.com.bogan.env.BoganEnvironment;
import br.com.bogan.factory.ComponentFactory;

public final class ContextRefreshedEvent extends BoganEvent {

    private final ComponentFactory factory;

    public ContextRefreshedEvent(Class<?> primaryClass, BoganEnvironment env, ComponentFactory factory) {
        super(primaryClass, env);
        this.factory = factory;
    }

    public ComponentFactory getFactory() {
        return factory;
    }
}
