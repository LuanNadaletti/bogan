package br.com.bogan.annotations;

import java.lang.annotation.*;

/**
 * Marks the primary entry point for component scanning within the application.
 * <p>
 * The {@code @ScanPoint} annotation is used to indicate the package from which the
 * framework should start scanning for components (e.g., classes annotated with
 * {@link Component}). Typically, this is placed on a root configuration or main
 * application class.
 * </p>
 *
 * <h2>Usage Guidelines</h2>
 * <ul>
 *   <li>Place on a top-level class in the base package you want to scan.</li>
 *   <li>Only one {@code @ScanPoint} should exist in the application to avoid ambiguity.</li>
 *   <li>Combines naturally with other annotations such as {@code @Component} and
 *       {@code @Inject} for dependency injection.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * @ScanPoint
 * public class ApplicationRoot {
 *     public static void main(String[] args) {
 *         Bogan.run(ApplicationRoot.class, args);
 *     }
 * }
 * }</pre>
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScanPoint {
}
