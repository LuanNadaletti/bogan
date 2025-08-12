package br.com.bogan.annotations;

import java.lang.annotation.*;

/**
 * Marks a constructor, field, or method parameter to be injected with a dependency
 * managed by the Bogan framework's dependency injection container.
 * <p>
 * The {@code @Inject} annotation can be placed on:
 * <ul>
 *   <li><b>Constructors</b> — to indicate that the constructor should be used for dependency injection.</li>
 *   <li><b>Fields</b> — to request that the framework automatically set the field's value.</li>
 *   <li><b>Parameters</b> — for fine-grained control of injected values in constructors or methods.</li>
 * </ul>
 * <p>
 * If multiple constructors are available, the framework will prefer the one annotated with {@code @Inject}.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * public class Service {
 *     @Inject
 *     private Repository repository;
 *
 *     @Inject
 *     public Service(Repository repository) {
 *         this.repository = repository;
 *     }
 * }
 * }</pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER})
public @interface Inject {
}
