package br.com.bogan.error;

import br.com.bogan.annotations.ScopeType;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.definition.DependencyRequirement;
import br.com.bogan.definition.InjectionMode;
import br.com.bogan.factory.ComponentFactory;
import br.com.bogan.registry.DefinitionRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class CircularDependencyTest {

    private ComponentDefinition def(String name, Class<?> type, List<DependencyRequirement> deps) {
        return new ComponentDefinition(name, type, ScopeType.SINGLETON, false, InjectionMode.CONSTRUCTOR, Set.of(), deps, 0);
    }

    @Test
    void detectsCircularDependency() {
        var reg = new DefinitionRegistry();
        reg.register(def("A", A.class, List.of(new DependencyRequirement(B.class, null, true, "constructor:param#0"))));
        reg.register(def("B", B.class, List.of(new DependencyRequirement(A.class, null, true, "constructor:param#0"))));
        var factory = new ComponentFactory(reg);
        assertThrows(CreationException.class, () -> factory.getComponent("A"));
    }

    static class A {
        A(B b) {
        }
    }

    static class B {
        B(A a) {
        }
    }
}
