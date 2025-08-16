package br.com.bogan.factory;

import br.com.bogan.annotations.Inject;
import br.com.bogan.annotations.ScopeType;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.definition.DependencyRequirement;
import br.com.bogan.definition.InjectionMode;
import br.com.bogan.error.AmbiguousDependencyException;
import br.com.bogan.error.NoSuchComponentException;
import br.com.bogan.registry.DefinitionRegistry;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ComponentFactoryIntegrationTest {

    private ComponentDefinition def(String name, Class<?> type, InjectionMode mode, List<DependencyRequirement> deps) {
        return new ComponentDefinition(name, type, ScopeType.SINGLETON, false, mode, Set.of(), deps, 0);
    }

    @Test
    void resolvesConstructorInjection() {
        DefinitionRegistry reg = new DefinitionRegistry();
        reg.register(def("repo", Repo.class, InjectionMode.CONSTRUCTOR, List.of()));
        reg.register(def("service", ServiceCtor.class, InjectionMode.CONSTRUCTOR,
                List.of(new DependencyRequirement(Repo.class, null, true, "constructor:param#0"))));

        ComponentFactory factory = new ComponentFactory(reg);
        ServiceCtor s = factory.getComponent(ServiceCtor.class);
        assertNotNull(s.repo);
    }

    @Test
    void resolvesFieldInjection() {
        DefinitionRegistry reg = new DefinitionRegistry();
        reg.register(def("repo", Repo.class, InjectionMode.CONSTRUCTOR, List.of()));
        reg.register(def("service", ServiceField.class, InjectionMode.FIELD, List.of()));

        ComponentFactory factory = new ComponentFactory(reg);
        ServiceField s = factory.getComponent(ServiceField.class);
        assertNotNull(s.repo);
    }

    @Test
    void ambiguousByTypeThrows() {
        DefinitionRegistry reg = new DefinitionRegistry();
        reg.register(def("repo1", Repo.class, InjectionMode.CONSTRUCTOR, List.of()));
        reg.register(def("repo2", RepoAlt.class, InjectionMode.CONSTRUCTOR, List.of()));
        ComponentFactory factory = new ComponentFactory(reg);
        assertThrows(AmbiguousDependencyException.class, () -> factory.getComponent(Repo.class));
    }

    @Test
    void missingBeanThrows() {
        DefinitionRegistry reg = new DefinitionRegistry();
        ComponentFactory factory = new ComponentFactory(reg);
        assertThrows(NoSuchComponentException.class, () -> factory.getComponent("x"));
    }

    static class Repo {
    }

    static class RepoAlt extends Repo {
    }

    static class ServiceCtor {
        final Repo repo;

        @Inject
        ServiceCtor(Repo repo) {
            this.repo = repo;
        }
    }

    static class ServiceField {
        @Inject
        Repo repo;
    }
}
