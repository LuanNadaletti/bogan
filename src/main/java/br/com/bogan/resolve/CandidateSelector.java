package br.com.bogan.resolve;

import br.com.bogan.annotations.Primary;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.error.AmbiguousDependencyException;
import br.com.bogan.error.NoSuchComponentException;

import java.util.List;
import java.util.Optional;

public class CandidateSelector {

    public Optional<ComponentDefinition> resolveCandidate(ResolverContext ctx, Class<?> type, String qualifier, boolean required) {
        if (type == java.util.Optional.class) {
            throw new UnsupportedOperationException("Handle Optional<T> at parameter metadata time");
        }

        List<ComponentDefinition> candidates = ctx.factory().getDefinitionsByType(type);

        if (candidates.isEmpty()) {
            if (required) throw new NoSuchComponentException(type.getName());
            return Optional.empty();
        }

        if (qualifier != null && !qualifier.isBlank()) {
            candidates = candidates.stream()
                    .filter(d -> d.getQualifiers().contains(qualifier))
                    .toList();

            if (candidates.isEmpty()) {
                if (required) throw new NoSuchComponentException(type.getName() + " (qualifier=" + qualifier + ")");
                return Optional.empty();
            }
        }

        List<ComponentDefinition> primaries = candidates.stream()
                .filter(d -> d.getComponentClass().isAnnotationPresent(Primary.class))
                .toList();

        if (primaries.size() > 1) {
            throw new AmbiguousDependencyException(type,
                    primaries.stream().map(ComponentDefinition::getName).toList());
        } else if (primaries.size() == 1) {
            return Optional.of(primaries.getFirst());
        }

        if (candidates.size() > 1) {
            throw new AmbiguousDependencyException(type,
                    candidates.stream().map(ComponentDefinition::getName).toList());
        }

        return Optional.of(candidates.getFirst());
    }
}
