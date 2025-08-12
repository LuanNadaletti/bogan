package br.com.bogan.error;

import java.util.Deque;
import java.util.stream.Collectors;

public class CircularDependencyException extends RuntimeException {
    public CircularDependencyException(Deque<String> path) {
        super("Circular dependency detected: " + path.stream().collect(Collectors.joining(" -> ")));
    }
}