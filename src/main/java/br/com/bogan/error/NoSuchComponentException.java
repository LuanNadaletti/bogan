package br.com.bogan.error;

public class NoSuchComponentException extends RuntimeException {
    public NoSuchComponentException(String name) {
        super("The component " + name + " does not exist");
    }
}
