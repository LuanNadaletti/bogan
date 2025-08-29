package br.com.bogan.core.events;

import br.com.bogan.env.BoganEnvironment;

public final class ContextLoadedEvent extends BoganEvent {

    public ContextLoadedEvent(Class<?> primaryClass, BoganEnvironment environment) {
        super(primaryClass, environment);
    }
}
