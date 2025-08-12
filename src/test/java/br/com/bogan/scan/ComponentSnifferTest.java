package br.com.bogan.scan;

import br.com.bogan.annotations.Component;
import br.com.bogan.annotations.Inject;
import br.com.bogan.annotations.ScanPoint;
import br.com.bogan.definition.ComponentDefinition;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ComponentSnifferTest {

    @Test
    void discoversComponentsUnderScanPoint() {
        var sniffer = new ComponentSniffer();
        Collection<ComponentDefinition> defs = sniffer.discover(ComponentSnifferTest.class);
        assertTrue(defs.stream().anyMatch(d -> d.getComponentClass().equals(Dep.class)));
        assertTrue(defs.stream().anyMatch(d -> d.getComponentClass().equals(Svc.class)));
    }

    @ScanPoint
    static class Root {
    }

    @Component
    static class Dep {
    }

    @Component
    static class Svc {
        @Inject
        Svc(Dep d) {
        }
    }
}
