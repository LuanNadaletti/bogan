package br.com.bogan.scan;

import br.com.bogan.definition.ComponentDefinition;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentSnifferTest {

    @Test
    void discoversComponentsOverScanPoint() {
        var sniffer = new ComponentSniffer();
        Collection<ComponentDefinition> defs = sniffer.discover(ComponentSnifferTest.class);
        assertTrue(defs.stream().anyMatch(d -> d.componentClass().equals(Service.Dep.class)));
        assertTrue(defs.stream().anyMatch(d -> d.componentClass().equals(Service.Svc.class)));
    }
}
