package br.com.bogan.error;

import java.util.List;

public class AmbiguousDependencyException extends RuntimeException {
    public AmbiguousDependencyException(Class<?> type, List<String> candidates) {
        super("Multiple candidates for type " + type.getName() + ": " + candidates);
    }
}
