package br.com.bogan.core.events;

import br.com.bogan.env.BoganEnvironment;

public abstract class BoganEvent {

    private final Class<?> primaryClass;
    private final BoganEnvironment environment;

    protected BoganEvent(Class<?> primaryClass, BoganEnvironment environment) {
        this.primaryClass = primaryClass;
        this.environment = environment;
    }

    public Class<?> getPrimaryClass() {
        return primaryClass;
    }

    public BoganEnvironment getEnvironment() {
        return environment;
    }
}
