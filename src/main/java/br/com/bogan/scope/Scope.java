package br.com.bogan.scope;

import java.util.function.Supplier;

public interface Scope {

    Object getOrCreate(String name, Supplier<Object> creationFn);
    void clear();
}
