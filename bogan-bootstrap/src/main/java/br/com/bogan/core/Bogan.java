package br.com.bogan.core;

import br.com.bogan.factory.ComponentFactory;

/**
 * Facade with high-level entrypoints to launch the Bogan application.
 */
public final class Bogan {

    private Bogan() {
    }

    public static ComponentFactory run(Class<?> primaryClass, String... args) {
        return new BoganApplication(primaryClass).run(args);
    }

    public static BoganContext runContext(Class<?> primaryClass, String... args) {
        return new BoganApplication(primaryClass).runContext(args);
    }

    public static BoganApplication application(Class<?> primaryClass) {
        return new BoganApplication(primaryClass);
    }
}
