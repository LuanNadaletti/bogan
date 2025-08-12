package br.com.bogan.error;

public class InjectionException extends RuntimeException {
    public InjectionException(String point, Throwable cause) {
        super("Injection failed at " + point, cause);
    }
}