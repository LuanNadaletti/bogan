package br.com.bogan.core;

import br.com.bogan.core.events.BoganEvent;
import br.com.bogan.env.BoganEnvironment;
import br.com.bogan.factory.ComponentFactory;

/**
 * Central contract representing the running Bogan application context.
 * Encapsulates the factory, environment and lifecycle operations.
 */
public interface BoganContext extends AutoCloseable {

    ComponentFactory getFactory();

    BoganEnvironment getEnvironment();

    void publishEvent(BoganEvent event);

    void addListener(BoganApplicationListener<?> listener);

    void removeListener(BoganApplicationListener<?> listener);

    @Override
    void close();

    boolean isClosed();
}
