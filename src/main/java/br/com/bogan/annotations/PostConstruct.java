package br.com.bogan.annotations;

import java.lang.annotation.*;

/**
 * Identifies a method to be executed after the framework has completed dependency injection
 * and the component instance has been fully constructed.
 * <p>
 * Methods annotated with {@code @PostConstruct} are typically used for initialization logic
 * that cannot be performed in the constructor, such as opening resources, validating configuration,
 * or triggering background processes.
 * </p>
 *
 * <h2>Usage Guidelines</h2>
 * <ul>
 *   <li>The method must have no parameters.</li>
 *   <li>The method can be private, protected, or public; access will be overridden if necessary.</li>
 *   <li>Only one {@code @PostConstruct} method is recommended per class.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * @Component
 * public class MyService {
 *     @PostConstruct
 *     private void init() {
 *         // initialization logic here
 *     }
 * }
 * }</pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostConstruct {
}
