package br.com.bogan.annotations;

import java.lang.annotation.*;

/**
 * Indicates that the annotated class is a candidate for automatic detection and registration
 * as a managed component within the Bogan framework's dependency injection container.
 * <p>
 * Classes annotated with {@code @Component} will be scanned and their metadata stored
 * in the {@code DefinitionRegistry} for later instantiation and injection.
 * The annotation supports configuration of the component's name, scope, and initialization strategy.
 * </p>
 * <h2>Usage</h2>
 * <pre>{@code
 * @Component(value = "myService", scope = ScopeType.SINGLETON, lazy = true)
 * public class MyService {
 *     // business logic here
 * }
 * }</pre>
 *
 * <h3>Attributes:</h3>
 * <ul>
 *   <li>{@code value} — Optional explicit component name; defaults to the simple class name if omitted.</li>
 *   <li>{@code scope} — Lifecycle scope of the component (e.g., {@code SINGLETON} or {@code PROTOTYPE}).</li>
 *   <li>{@code lazy} — Whether the component should be lazily initialized on first access rather than at context startup.</li>
 * </ul>
 *
 * @see ScopeType
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

    /**
     * Explicit component name for registry lookup.
     * If empty, the framework will use the class name.
     */
    String value() default "";

    /**
     * Defines the lifecycle scope of the component.
     */
    ScopeType scope() default ScopeType.PROTOTYPE;

    /**
     * Indicates whether the component should be lazily initialized.
     */
    boolean lazy() default false;
}
