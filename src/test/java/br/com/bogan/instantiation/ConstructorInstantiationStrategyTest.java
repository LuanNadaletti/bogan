package br.com.bogan.instantiation;

import br.com.bogan.annotations.Inject;
import br.com.bogan.annotations.ScopeType;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.definition.InjectionMode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConstructorInstantiationStrategyTest {

    private ComponentDefinition def(Class<?> c) {
        return new ComponentDefinition(c.getSimpleName(), c, ScopeType.SINGLETON, false, InjectionMode.CONSTRUCTOR, Set.of(), List.of(), 0);
    }

    @Test
    void choosesInjectAnnotatedConstructor() throws Exception {
        var strat = new ConstructorInstantiationStrategy();
        var d = def(OneArg.class);
        Object obj = strat.instantiate(d, new Object[]{"x"});
        assertTrue(obj instanceof OneArg);
        assertEquals("x", ((OneArg) obj).s);
    }

    @Test
    void fallsBackToNoArg() throws Exception {
        var strat = new ConstructorInstantiationStrategy();
        var d = def(NoArg.class);
        Object obj = strat.instantiate(d, new Object[]{});
        assertTrue(obj instanceof NoArg);
    }

    @Test
    void matchesByArityWhenNoInject() throws Exception {
        var strat = new ConstructorInstantiationStrategy();
        var d = def(Multi.class);
        Object obj = strat.instantiate(d, new Object[]{"x"});
        assertTrue(obj instanceof Multi);
    }

    static class NoArg {
        NoArg() {
        }
    }

    static class OneArg {
        final String s;

        @Inject
        OneArg(String s) {
            this.s = s;
        }
    }

    static class Multi {
        Multi() {
        }

        Multi(String s) {
        }
    }
}
