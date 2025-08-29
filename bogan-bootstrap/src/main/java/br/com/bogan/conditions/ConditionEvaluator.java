package br.com.bogan.conditions;

import br.com.bogan.env.BoganEnvironment;

import java.util.Arrays;
import java.util.List;

public class ConditionEvaluator {

    private ConditionEvaluator() {
    }

    public static boolean matches(Class<?> type, BoganEnvironment env) {
        ConditionalOnProperty onProperty = type.getAnnotation(ConditionalOnProperty.class);
        if (onProperty != null) {
            String val = env.getProperty(onProperty.value());
            if (val == null) {
                if (!onProperty.matchIfMissing()) return false;
            } else if (!val.equalsIgnoreCase(onProperty.havingValue())) {
                return false;
            }
        }

        ConditionalOnClass onClass = type.getAnnotation(ConditionalOnClass.class);
        if (onClass != null) {
            for (String cn : onClass.value()) {
                try {
                    Class.forName(cn);
                } catch (ClassNotFoundException e) {
                    return false;
                }
            }
        }

        ConditionalOnProfile onProfile = type.getAnnotation(ConditionalOnProfile.class);
        if (onProfile != null) {
            List<String> active = env.getActiveProfiles();
            return Arrays.stream(onProfile.value()).anyMatch(active::contains);
        }

        return true;
    }
}
