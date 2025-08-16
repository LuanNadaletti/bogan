package br.com.bogan.resolve;

import br.com.bogan.annotations.Inject;
import br.com.bogan.annotations.Qualifier;
import br.com.bogan.definition.*;
import br.com.bogan.error.InjectionException;
import br.com.bogan.error.NoSuchComponentException;
import br.com.bogan.resolve.injection.SpecialInjectionAdapter;
import br.com.bogan.scan.ReflectionModeCache;
import br.com.bogan.util.ComponentNameUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReflectiveDependencyResolver implements DependencyResolver {

    private final List<SpecialInjectionAdapter> adapters;
    private final CandidateSelector selector;
    private final ReflectionModeCache cache;

    public ReflectiveDependencyResolver(List<SpecialInjectionAdapter> adapters, CandidateSelector selector, ReflectionModeCache cache) {
        this.adapters = adapters;
        this.selector = selector;
        this.cache = cache;
    }

    @Override
    public Object[] resolveConstructorArgs(ComponentDefinition def, ResolverContext ctx) {
        InjectionModel model = cache.getOrBuild(def.getComponentClass());
        List<InjectionPoint> ctorParams = model.getCtorParams();
        if (ctorParams.isEmpty()) return new Object[0];

        Object[] args = new Object[ctorParams.size()];

        for (int i = 0; i < ctorParams.size(); i++) {
            InjectionPoint p = ctorParams.get(i);

            Optional<Object> adapted = tryAdapters(p, ctx);
            if (adapted.isPresent()) {
                args[i] = adapted.get();
                continue;
            }

            Optional<Object> selected = resolveByTypeAndQualifier(ctx, p.getRawType(), p.getQualifier(), p.isRequired());

            if (selected.isPresent()) {
                args[i] = selected.get();
                continue;
            }

            args[i] = null;
        }

        return args;
    }

    @Override
    public void injectFields(Object target, ComponentDefinition def, ResolverContext ctx) {
        if (def.getInjectionMode() == InjectionMode.CONSTRUCTOR) return;
        for (Field f : def.getComponentClass().getDeclaredFields()) {
            if (f.isAnnotationPresent(Inject.class)) {
                InjectionPoint injectionPoint = InjectionPoint.from(f);
                Object dep = getResolvedByAdapterOrFactory(f, ctx, injectionPoint);

                try {
                    f.setAccessible(true);
                    f.set(target, dep);
                } catch (IllegalAccessException e) {
                    throw new InjectionException("field:" + f.getName(), e);
                }
            }
        }
    }

    private Optional<Object> resolveByTypeAndQualifier(ResolverContext ctx, Class<?> type, String qualifier, boolean required) {
        Optional<ComponentDefinition> def = selector.resolveCandidate(ctx, type, qualifier, required);

        if (def.isEmpty() && required) {
            throw new NoSuchComponentException(ComponentNameUtils.formatNameAndQualifier(type, qualifier));
        }

        return Optional.of(ctx.factory().getByDefinition(def.get(), ctx));
    }

    private Optional<Object> tryAdapters(InjectionPoint p, ResolverContext ctx) {
        for (SpecialInjectionAdapter adapter : adapters) {
            if (adapter.supports(p)) {
                return Optional.ofNullable(adapter.resolve(p, ctx));
            }
        }
        return Optional.empty();
    }

    private Object getResolvedByAdapterOrFactory(Field field, ResolverContext ctx, InjectionPoint injectionPoint) {
        Optional<Object> fromAdapter = adapters.stream()
                .filter(adapter -> adapter.supports(injectionPoint))
                .map(adapter -> adapter.resolve(injectionPoint, ctx))
                .findFirst();

        if (fromAdapter.isPresent()) {
            return fromAdapter.get();
        }

        String q = field.isAnnotationPresent(Qualifier.class) ? field.getAnnotation(Qualifier.class).value() : null;
        return resolveByTypeAndQualifier(ctx, field.getType(), q, true)
                .orElseThrow(() -> new NoSuchComponentException(injectionPoint.getMemberName()));
    }
}
