package br.com.bogan.scope;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SingletonScope implements Scope {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public Object getOrCreate(String name, Supplier<Object> creationFn) {
        return cache.computeIfAbsent(name, key -> creationFn.get());
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
