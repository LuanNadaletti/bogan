package br.com.bogan.annotations;

/**
 * Defines the available scopes for a component within the framework's lifecycle.
 * <p>
 * Scope determines how and when component instances are created and reused by the container.
 * </p>
 *
 * <h2>Scopes</h2>
 * <ul>
 *   <li>{@link #SINGLETON} — A single shared instance is created and reused for all requests.</li>
 *   <li>{@link #PROTOTYPE} — A new instance is created each time the component is requested.</li>
 * </ul>
 *
 * <h2>Example</h2>
 * <pre>{@code
 * @Component(scope = ScopeType.SINGLETON)
 * public class MyService {
 *     // service implementation
 * }
 * }</pre>
 */
public enum ScopeType {
    /**
     * A single shared instance for the entire application context.
     */
    SINGLETON,

    /**
     * A new instance is created for every injection or lookup request.
     */
    PROTOTYPE
}
