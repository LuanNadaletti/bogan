package br.com.bogan.scan;

import br.com.bogan.definition.InjectionModel;

import java.util.HashMap;
import java.util.Map;

public class ReflectionModeCache {

    private final Map<Class<?>, InjectionModel> cache;

    public ReflectionModeCache() {
        this.cache = new HashMap<>();
    }

    public void put(Class<?> clazz) {
        cache.put(clazz, new InjectionModel(clazz));
    }

    public InjectionModel get(Class<?> clazz) {
        return cache.get(clazz);
    }

    public InjectionModel getOrBuild(Class<?> clazz) {
        return cache.containsKey(clazz) ? cache.get(clazz) : new InjectionModel(clazz);
    }
}
