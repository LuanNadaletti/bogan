package br.com.bogan.example;

import br.com.bogan.core.events.BoganEvent;
import br.com.bogan.env.BoganEnvironment;

public final class MyCustomEvent extends BoganEvent {

    MyCustomEvent(Class<?> primaryClass, BoganEnvironment environment) {
        super(primaryClass, environment);
    }
}
