package br.com.bogan.error;

public class DuplicateDefinitionException extends RuntimeException {
    public DuplicateDefinitionException(String name) { super("Duplicate component definition: '" + name + "'"); }
}
