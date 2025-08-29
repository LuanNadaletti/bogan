package br.com.bogan.core;

import br.com.bogan.core.events.BoganEvent;
import br.com.bogan.core.events.ContextClosedEvent;
import br.com.bogan.env.BoganEnvironment;
import br.com.bogan.factory.ComponentFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultBoganContext implements BoganContext {

    private final Class<?> primaryClass;
    private final BoganEnvironment environment;
    private final ComponentFactory factory;
    private final SimpleApplicationEventMulticaster multicaster;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    DefaultBoganContext(Class<?> primaryClass,
                        BoganEnvironment environment,
                        ComponentFactory factory,
                        SimpleApplicationEventMulticaster multicaster) {
        this.primaryClass = Objects.requireNonNull(primaryClass);
        this.environment = Objects.requireNonNull(environment);
        this.factory = Objects.requireNonNull(factory);
        this.multicaster = Objects.requireNonNull(multicaster);
    }

    @Override
    public ComponentFactory getFactory() {
        return factory;
    }

    @Override
    public BoganEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void publishEvent(BoganEvent event) {
        multicaster.publishEvent(event);
    }

    @Override
    public void addListener(BoganApplicationListener<?> listener) {
        multicaster.addListener(listener);
    }

    @Override
    public void removeListener(BoganApplicationListener<?> listener) {
        multicaster.removeListener(listener);
    }

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            multicaster.publishEvent(new ContextClosedEvent(primaryClass, environment, factory));
            factory.shutdown();
        }
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }
}
