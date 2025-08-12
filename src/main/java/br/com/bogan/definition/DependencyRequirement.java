package br.com.bogan.definition;

import java.util.Objects;

public class DependencyRequirement {

    private final Class<?> type;
    private final String qualifier;
    private final boolean required;
    private final String injectionPoint;

    public DependencyRequirement(Class<?> type, String qualifier, boolean required, String injectionPoint) {
        this.type = Objects.requireNonNull(type, "type");
        this.qualifier = qualifier;
        this.required = required;
        this.injectionPoint = Objects.requireNonNull(injectionPoint, "injectionPoint");
    }

    public Class<?> getType() {
        return type;
    }

    public String getQualifier() {
        return qualifier;
    }

    public boolean isRequired() {
        return required;
    }

    public String getInjectionPoint() {
        return injectionPoint;
    }
}
