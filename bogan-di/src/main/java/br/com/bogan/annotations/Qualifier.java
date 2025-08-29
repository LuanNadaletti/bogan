package br.com.bogan.annotations;

import java.lang.annotation.*;

/**
 * Specifies a custom qualifier for resolving dependency injection ambiguities.
 * <p>
 * The {@code @Qualifier} annotation can be applied to:
 * <ul>
 *   <li>Fields — to indicate which specific bean to inject when multiple candidates exist.</li>
 *   <li>Parameters — for constructor or method injection targeting a named bean.</li>
 *   <li>Types — to mark a component definition with a specific qualifier name.</li>
 * </ul>
 * This is especially useful when multiple beans of the same type are present in the context,
 * and you need to explicitly declare which one should be injected.
 * </p>
 *
 * <h2>Example Usage</h2>
 * <pre>{@code
 * @Component
 * @Qualifier("fastService")
 * public class FastServiceImpl implements Service {
 *     // implementation
 * }
 *
 * public class Client {
 *     @Inject
 *     public Client(@Qualifier("fastService") Service service) {
 *         this.service = service;
 *     }
 * }
 * }</pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.TYPE})
public @interface Qualifier {

    /**
     * The qualifier value used to distinguish between beans.
     * @return the qualifier name
     */
    String value();
}
