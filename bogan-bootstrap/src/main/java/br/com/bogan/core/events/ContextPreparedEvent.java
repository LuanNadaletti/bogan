package br.com.bogan.core.events;

import br.com.bogan.env.BoganEnvironment;

public final class ContextPreparedEvent extends BoganEvent {

    public ContextPreparedEvent(Class<?> primaryClass, BoganEnvironment env) {
        super(primaryClass, env);
    }
}

