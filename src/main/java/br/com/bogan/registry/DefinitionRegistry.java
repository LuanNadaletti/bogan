package br.com.bogan.registry;

import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.error.DuplicateDefinitionException;
import br.com.bogan.error.NoSuchComponentException;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class DefinitionRegistry {

    private final Map<String, ComponentDefinition> byName = new ConcurrentHashMap<>();

    public void register(ComponentDefinition def) {
        Objects.requireNonNull(def, "definition");
        ComponentDefinition prev = byName.putIfAbsent(def.name(), def);

        if (prev != null)
            throw new DuplicateDefinitionException(def.name());
    }

    public void registerAll(Collection<ComponentDefinition> defs) {
        defs.forEach(this::register);
    }

    public ComponentDefinition get(String componentName) {
        ComponentDefinition def = byName.get(componentName);
        if (def == null)
            throw new NoSuchComponentException(componentName);

        return def;
    }

    public Collection<ComponentDefinition> all() {
        return Collections.unmodifiableCollection(byName.values());
    }
}
