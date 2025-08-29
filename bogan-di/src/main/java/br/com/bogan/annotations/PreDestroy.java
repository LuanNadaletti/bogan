package br.com.bogan.annotations;

import java.lang.annotation.*;

/**
 * Identifies a method to be invoked by the framework just before a managed component
 * is removed from the container or the application context is shut down.
 * <p>
 * Methods annotated with {@code @PreDestroy} are intended for cleanup operations,
 * such as releasing resources, closing connections, or stopping background tasks.
 * </p>
 *
 * <h2>Usage Guidelines</h2>
 * <ul>
 *   <li>The method must have no parameters.</li>
 *   <li>The method can be private, protected, or public; the framework will override access if needed.</li>
 *   <li>Only one {@code @PreDestroy} method is recommended per class.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * @Component
 * public class MyService {
 *     @PreDestroy
 *     private void cleanup() {
 *         // cleanup logic here
 *     }
 * }
 * }</pre>
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreDestroy {
}
