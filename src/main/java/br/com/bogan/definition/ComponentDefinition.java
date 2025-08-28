package br.com.bogan.definition;

import br.com.bogan.annotations.ScopeType;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Immutable metadata describing a component managed by the Bogan container.
 * <p>
 * A {@code ComponentDefinition} captures the container-facing contract of a component:
 * its logical {@link #name () name}, implementation {@link #componentClass () class},
 * lifecycle {@link #scope () scope}, initialization policy ({@link #lazy ()}),
 * injection strategy ({@link #injectionMode ()}), optional type
 * {@link #qualifiers () qualifiers}, ordered {@link #dependencies () dependencies}
 * and its initialization {@link #order () order}.
 * </p>
 *
 * <h2>Semantics &amp; Invariants</h2>
 * <ul>
 *   <li>The definition is <b>immutable</b> after construction.</li>
 *   <li>{@code name} must be non-blank and unique within a registry.</li>
 *   <li>{@code componentClass} must be a concrete class (not an interface/abstract).</li>
 *   <li>{@code dependencies} are ordered to match constructor parameters for
 *       {@link InjectionMode#CONSTRUCTOR} (or mixed) injection.</li>
 *   <li>{@code qualifiers} are optional tags used to disambiguate beans of the same type.</li>
 * </ul>
 *
 * <h2>Typical Usage</h2>
 * <pre>{@code
 * ComponentDefinition def = new ComponentDefinition(
 *     "orderService",
 *     OrderService.class,
 *     ScopeType.SINGLETON,
 *     false,                       // eager initialization
 *     InjectionMode.CONSTRUCTOR,   // inject via constructor
 *     Set.of("primary"),
 *     List.of(                     // constructor args: (OrderRepo repo, PaymentGateway gw)
 *         new DependencyRequirement(OrderRepo.class, null, true, "constructor:param#0"),
 *         new DependencyRequirement(PaymentGateway.class, "stripe", true, "constructor:param#1")
 *     ),
 *     0                            // initialization order
 * );
 * }</pre>
 */
public record ComponentDefinition(String name, Class<?> componentClass, ScopeType scope, boolean lazy,
                                  InjectionMode injectionMode, Set<String> qualifiers,
                                  List<DependencyRequirement> dependencies, int order) {

    /**
     * Creates a new immutable component definition.
     *
     * @param name           logical bean name (must be unique in a registry and non-blank)
     * @param componentClass concrete implementation class (not abstract/interface)
     * @param scope          lifecycle scope (e.g. {@link ScopeType#SINGLETON})
     * @param lazy           whether the bean is initialized on first access instead of at startup
     * @param injectionMode  injection strategy (constructor, field, or mixed)
     * @param qualifiers     optional disambiguation tags (never {@code null})
     * @param dependencies   ordered dependency requirements (never {@code null})
     * @param order          initialization order; lower values are initialized earlier
     * @throws IllegalArgumentException if {@code name} is blank or {@code componentClass} is not concrete
     * @throws NullPointerException     if a required argument is {@code null}
     */
    public ComponentDefinition(
            String name,
            Class<?> componentClass,
            ScopeType scope,
            boolean lazy,
            InjectionMode injectionMode,
            Set<String> qualifiers,
            List<DependencyRequirement> dependencies,
            int order) {
        this.name = Objects.requireNonNull(name, "name");
        this.componentClass = Objects.requireNonNull(componentClass, "componentClass");
        this.scope = Objects.requireNonNull(scope, "scope");
        this.lazy = lazy;
        this.injectionMode = Objects.requireNonNull(injectionMode, "injectionMode");
        this.qualifiers = Set.copyOf(qualifiers == null ? Set.of() : qualifiers);
        this.dependencies = List.copyOf(dependencies == null ? List.of() : dependencies);
        this.order = order;

        if (componentClass.isInterface())
            throw new IllegalArgumentException("Component class must be concrete: " + componentClass);
        if (name.isBlank())
            throw new IllegalArgumentException("Component name cannot be blank");
    }

    @Override
    public String toString() {
        return "ComponentDefinition{" + name + ":" + componentClass.getName() + ", scope=" + scope + ", lazy=" + lazy + ", mode=" + injectionMode + "}";
    }
}
