package br.com.bogan.scope;

import java.util.function.Supplier;

public class PrototypeScope implements Scope {

    @Override
    public Object getOrCreate(String name, Supplier<Object> creationFn) {
        return creationFn.get();
    }

    @Override
    public void clear() {
    }
}
