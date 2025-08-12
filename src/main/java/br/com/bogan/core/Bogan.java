package br.com.bogan.core;

import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.factory.ComponentFactory;
import br.com.bogan.registry.DefinitionRegistry;
import br.com.bogan.scan.ComponentSniffer;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Central entry point for bootstrapping a Bogan application.
 * <p>
 * The {@code Bogan} class is responsible for initializing the component registry,
 * discovering components, creating the {@link ComponentFactory}, and starting the
 * application context. It mimics the behavior of well-known application bootstrap
 * mechanisms (such as Spring Boot's {@code SpringApplication.run}).
 * </p>
 *
 * <h2>Usage Example</h2>
 * <pre>{@code
 * @ScanPoint
 * public class MyApp {
 *     public static void main(String[] args) {
 *         ComponentFactory factory = Bogan.run(MyApp.class, args);
 *         MyService service = factory.getComponent(MyService.class);
 *         service.execute();
 *     }
 * }
 * }</pre>
 */
public final class Bogan {

    private final ComponentFactory factory;

    private Bogan(ComponentFactory factory) {
        this.factory = factory;
        // Ensure proper shutdown behavior when JVM exits
        Runtime.getRuntime().addShutdownHook(new Thread(factory::shutdown, "bogan-shutdown"));
    }

    /**
     * Bootstraps the application context using the specified primary class.
     *
     * @param primaryClass the main class annotated with {@code @ScanPoint}
     * @param args         optional application arguments
     * @return the initialized {@link ComponentFactory}
     */
    public static ComponentFactory run(Class<?> primaryClass, String... args) {
        return run(primaryClass, null, args);
    }

    /**
     * Bootstraps the application context with optional registry customization.
     *
     * @param primaryClass       the main class annotated with {@code @ScanPoint}
     * @param registryCustomizer optional customization hook for the {@link DefinitionRegistry}
     * @param args               optional application arguments
     * @return the initialized {@link ComponentFactory}
     */
    public static ComponentFactory run(Class<?> primaryClass, Consumer<DefinitionRegistry> registryCustomizer, String... args) {
        Objects.requireNonNull(primaryClass, "primaryClass");

        var sniffer = new ComponentSniffer();
        Collection<ComponentDefinition> defs = sniffer.discover(primaryClass);

        var registry = new DefinitionRegistry();
        if (registryCustomizer != null) registryCustomizer.accept(registry);
        registry.registerAll(defs);

        var factory = new ComponentFactory(registry);
        factory.initialize();

        return new Bogan(factory).getFactory();
    }

    /**
     * @return the underlying {@link ComponentFactory} instance
     */
    public ComponentFactory getFactory() {
        return factory;
    }
}
