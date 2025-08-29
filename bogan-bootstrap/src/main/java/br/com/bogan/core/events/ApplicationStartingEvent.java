package br.com.bogan.core.events;

import br.com.bogan.env.BoganEnvironment;

public final class ApplicationStartingEvent extends BoganEvent {

    public ApplicationStartingEvent(Class<?> primaryClass, BoganEnvironment environment) {
        super(primaryClass, environment);
    }
}
