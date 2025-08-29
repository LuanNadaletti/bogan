package br.com.bogan.registry;

import br.com.bogan.annotations.ScopeType;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.definition.InjectionMode;
import br.com.bogan.error.DuplicateDefinitionException;
import br.com.bogan.error.NoSuchComponentException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DefinitionRegistryTest {

    private ComponentDefinition def(String name, Class<?> type) {
        return new ComponentDefinition(name, type, ScopeType.SINGLETON, false, InjectionMode.CONSTRUCTOR, Set.of(), List.of(), 0);
    }

    @Test
    void registerAndGetName() {
        var reg = new DefinitionRegistry();
        var d = def("foo", String.class);
        reg.register(d);
        assertSame(d, reg.get("foo"));
    }

    @Test
    void duplicateDefinitionThrows() {
        var reg = new DefinitionRegistry();
        var d = def("foo", String.class);
        reg.register(d);
        assertThrows(DuplicateDefinitionException.class, () -> reg.register(d));
    }

    @Test
    void missingDefinitionThrows() {
        var reg = new DefinitionRegistry();
        assertThrows(NoSuchComponentException.class, () -> reg.get("missing"));
    }
}
