package br.com.bogan.definition;

import br.com.bogan.annotations.ScopeType;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ComponentDefinition {

    private final String name;
    private final Class<?> componentClass;
    private final ScopeType scope;
    private final boolean lazy;
    private final InjectionMode injectionMode;
    private final Set<String> qualifiers;
    private final List<DependencyRequirement> dependencies;
    private final int order;

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
        if (name.isBlank()) throw new IllegalArgumentException("Component name cannot be blank");
    }

    public String getName() {
        return name;
    }

    public Class<?> getComponentClass() {
        return componentClass;
    }

    public ScopeType getScope() {
        return scope;
    }

    public boolean isLazy() {
        return lazy;
    }

    public InjectionMode getInjectionMode() {
        return injectionMode;
    }

    public Set<String> getQualifiers() {
        return qualifiers;
    }

    public List<DependencyRequirement> getDependencies() {
        return dependencies;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return "ComponentDefinition{" + name + ":" + componentClass.getName() + ", scope=" + scope + ", lazy=" + lazy + ", mode=" + injectionMode + "}";
    }
}
