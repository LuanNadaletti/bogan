package br.com.bogan.definition;

import br.com.bogan.annotations.ScopeType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentDefinitionTest {

    @Test
    void createComponentWithInterfaceShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new ComponentDefinition("name", TestInterface.class,
                ScopeType.SINGLETON, false, InjectionMode.CONSTRUCTOR, Set.of(), List.of(), 0));
    }

    @Test
    void createComponentWithBlankNameShouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> new ComponentDefinition("",
                ComponentDefinitionTest.class, ScopeType.SINGLETON, false, InjectionMode.CONSTRUCTOR, Set.of(),
                List.of(), 0));
    }

    private interface TestInterface {
    }
}
