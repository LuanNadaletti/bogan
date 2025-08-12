package br.com.bogan.factory;

import br.com.bogan.annotations.PostConstruct;
import br.com.bogan.annotations.ScopeType;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.error.AmbiguousDependencyException;
import br.com.bogan.error.CircularDependencyException;
import br.com.bogan.error.CreationException;
import br.com.bogan.error.NoSuchComponentException;
import br.com.bogan.instantiation.ConstructorInstantiationStrategy;
import br.com.bogan.instantiation.InstantiationStrategy;
import br.com.bogan.registry.DefinitionRegistry;
import br.com.bogan.resolve.DependencyResolver;
import br.com.bogan.resolve.ReflectiveDependencyResolver;
import br.com.bogan.resolve.ResolverContext;
import br.com.bogan.scope.PrototypeScope;
import br.com.bogan.scope.Scope;
import br.com.bogan.scope.SingletonScope;

import java.lang.reflect.Method;
import java.util.*;

public class ComponentFactory {

    private final DefinitionRegistry registry;
    private final Map<ScopeType, Scope> scopes = new EnumMap<>(ScopeType.class);
    private final InstantiationStrategy instantiation = new ConstructorInstantiationStrategy();
    private final DependencyResolver resolver = new ReflectiveDependencyResolver();

    public ComponentFactory(DefinitionRegistry registry) {
        this.registry = Objects.requireNonNull(registry, "registry");
        scopes.put(ScopeType.SINGLETON, new SingletonScope());
        scopes.put(ScopeType.PROTOTYPE, new PrototypeScope());
    }

    public void register(ComponentDefinition def) {
        registry.register(def);
    }

    public void registerAll(Collection<ComponentDefinition> defs) {
        registry.registerAll(defs);
    }

    public Object getComponent(String name) {
        return getComponent(name, new ResolverContext(this));
    }

    public <T> T getComponent(Class<T> type) {
        return getComponent(type, null);
    }

    public <T> T getComponent(Class<T> type, String qualifier) {
        return getComponent(type, qualifier, new ResolverContext(this));
    }

    Object getComponent(String name, ResolverContext ctx) {
        ComponentDefinition def = registry.get(name);
        return getByDefinition(def, ctx);
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> type, String qualifier, ResolverContext ctx) {
        var matches = registry.all().stream()
                .filter(d -> type.isAssignableFrom(d.getComponentClass()))
                .filter(d -> qualifier == null || d.getQualifiers().contains(qualifier))
                .toList();
        if (matches.isEmpty()) throw new NoSuchComponentException(type.getName());
        if (matches.size() > 1)
            throw new AmbiguousDependencyException(type, matches.stream().map(ComponentDefinition::getName).toList());
        return (T) getByDefinition(matches.get(0), ctx);
    }

    private Object getByDefinition(ComponentDefinition def, ResolverContext ctx) {
        if (ctx.contains(def.getName())) {
            throw new CreationException(def.getName(), new CircularDependencyException(ctx.snapshot()));
        }
        Scope scope = scopes.get(def.getScope());
        return scope.getOrCreate(def.getName(), () -> create(def, ctx));
    }

    private Object create(ComponentDefinition def, ResolverContext ctx) {
        ctx.push(def.getName());
        try {
            Object[] args = resolver.resolveConstructorArgs(def, ctx);
            Object instance = instantiationSafe(def, args);
            resolver.injectFields(instance, def, ctx);
            invokePostConstructor(instance);
            return instance;
        } finally {
            ctx.pop();
        }
    }

    private Object instantiationSafe(ComponentDefinition def, Object[] args) {
        try {
            return instantiation.instantiate(def, args);
        } catch (Throwable t) {
            throw new CreationException(def.getName(), t);
        }
    }

    private void invokePostConstructor(Object instance) {
        for (Method m : instance.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(PostConstruct.class) && m.getParameterCount() == 0) {
                try {
                    m.setAccessible(true);
                    m.invoke(instance);
                } catch (Exception e) {
                    throw new CreationException(instance.getClass().getName() + ".@PostConstruct", e);
                }
            }
        }
    }

    public void shutdown() {
        // TODO: track created singletons to invoke @PreDestroy predictably.
    }

    public void initialize() {
        registry.all().stream()
                .filter(d -> d.getScope() == ScopeType.SINGLETON && !d.isLazy())
                .sorted(Comparator.comparingInt(ComponentDefinition::getOrder))
                .forEach(d -> getByDefinition(d, new ResolverContext(this)));
    }
}
