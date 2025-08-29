package br.com.bogan.error;

public class CreationException extends RuntimeException {
    public CreationException(String name, Throwable cause) {
        super("Failed to create component '" + name + "'", cause);
    }
}