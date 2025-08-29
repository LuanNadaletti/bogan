package br.com.bogan.util;

import br.com.bogan.definition.ComponentDefinition;

public class ComponentNameUtils {

    private ComponentNameUtils() {
    }

    public static String formatNameAndQualifier(Class<?> type, String qualifier) {
        if (qualifier == null || qualifier.isBlank()) {
            return type.getName();
        }
        return type.getName() + " (qualifier=" + qualifier + ")";
    }

    public static String formatDefinitionName(ComponentDefinition def) {
        String base = def.name();
        if (!def.qualifiers().isEmpty()) {
            base += " " + def.qualifiers();
        }
        return base + " [" + def.componentClass().getName() + "]";
    }
}
