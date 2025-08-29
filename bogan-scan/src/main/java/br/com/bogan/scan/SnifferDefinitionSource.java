package br.com.bogan.scan;

import br.com.bogan.core.DefinitionSource;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.env.BoganEnvironment;

import java.util.Collection;
import java.util.Objects;

public final class SnifferDefinitionSource implements DefinitionSource {

    private final ComponentSniffer sniffer = new ComponentSniffer();

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Collection<ComponentDefinition> load(Class<?> primaryClass, BoganEnvironment environment) {
        Objects.requireNonNull(primaryClass, "primaryClass");
        return sniffer.discover(primaryClass);
    }
}
