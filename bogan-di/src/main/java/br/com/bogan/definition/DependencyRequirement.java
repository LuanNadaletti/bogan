package br.com.bogan.definition;

import java.util.Objects;

public record DependencyRequirement(Class<?> type, String qualifier, boolean required, String injectionPoint) {

    public DependencyRequirement(Class<?> type, String qualifier, boolean required, String injectionPoint) {
        this.type = Objects.requireNonNull(type, "type");
        this.qualifier = qualifier;
        this.required = required;
        this.injectionPoint = Objects.requireNonNull(injectionPoint, "injectionPoint");
    }
}
