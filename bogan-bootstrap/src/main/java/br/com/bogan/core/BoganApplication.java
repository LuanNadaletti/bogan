package br.com.bogan.core;

import br.com.bogan.core.events.*;
import br.com.bogan.definition.ComponentDefinition;
import br.com.bogan.env.BoganEnvironment;
import br.com.bogan.env.DefaultBoganEnvironment;
import br.com.bogan.env.EnvironmentPostProcessor;
import br.com.bogan.factory.BoganFactoryPostProcessor;
import br.com.bogan.factory.ComponentFactory;
import br.com.bogan.registry.BoganDefinitionPostProcessor;
import br.com.bogan.registry.BoganRegistryCustomizer;
import br.com.bogan.registry.DefinitionRegistry;
import br.com.bogan.util.OrderUtils;
import br.com.bogan.util.ServiceLoaderUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class BoganApplication {

    private final Class<?> primaryClass;
    private final List<BoganRegistryCustomizer> registryCustomizers = new CopyOnWriteArrayList<>();
    private final List<BoganDefinitionPostProcessor> definitionPostProcessors = new CopyOnWriteArrayList<>();
    private final List<BoganFactoryPostProcessor> factoryPostProcessors = new CopyOnWriteArrayList<>();
    private final List<BoganApplicationListener<? extends BoganEvent>> listeners = new CopyOnWriteArrayList<>();
    private final List<EnvironmentPostProcessor> environmentPostProcessors = new CopyOnWriteArrayList<>();
    private final List<String> activeProfiles = new ArrayList<>();
    private final Properties defaultProperties = new Properties();

    public BoganApplication(Class<?> primaryClass) {
        this.primaryClass = Objects.requireNonNull(primaryClass, "primaryClass");
    }

    public BoganApplication registryCustomizers(BoganRegistryCustomizer... customizers) {
        if (customizers != null) this.registryCustomizers.addAll(Arrays.asList(customizers));
        return this;
    }

    public BoganApplication definitionPostProcessors(BoganDefinitionPostProcessor... postProcessors) {
        if (postProcessors != null) this.definitionPostProcessors.addAll(Arrays.asList(postProcessors));
        return this;
    }

    public BoganApplication factoryPostProcessors(BoganFactoryPostProcessor... postProcessors) {
        if (postProcessors != null) this.factoryPostProcessors.addAll(Arrays.asList(postProcessors));
        return this;
    }

    public BoganApplication listeners(BoganApplicationListener<? extends BoganEvent>... listeners) {
        if (listeners != null) this.listeners.addAll(Arrays.asList(listeners));
        return this;
    }

    public BoganApplication environmentPostProcessors(EnvironmentPostProcessor... postProcessors) {
        if (postProcessors != null) this.environmentPostProcessors.addAll(Arrays.asList(postProcessors));
        return this;
    }

    public BoganApplication profiles(String... profiles) {
        if (profiles != null) this.activeProfiles.addAll(Arrays.asList(profiles));
        return this;
    }

    public BoganApplication defaultProperties(Properties props) {
        if (props != null) this.defaultProperties.putAll(props);
        return this;
    }

    public ComponentFactory run(String... args) {
        return runContext(args).getFactory();
    }

    public BoganContext runContext(String... args) {
        BoganEnvironment environment = new DefaultBoganEnvironment();
        environment.addDefaultProperties(defaultProperties);
        environment.setActiveProfiles(this.activeProfiles);
        environment.applyArgs(args);

        List<EnvironmentPostProcessor> envPPs = ServiceLoaderUtils.loadOrdered(EnvironmentPostProcessor.class);
        envPPs.addAll(this.environmentPostProcessors);
        OrderUtils.sort(envPPs);

        SimpleApplicationEventMulticaster multicaster = new SimpleApplicationEventMulticaster();
        List<BoganApplicationListener> discovered = ServiceLoaderUtils.loadOrdered(BoganApplicationListener.class);
        discovered.forEach(multicaster::addListener);
        this.listeners.forEach(multicaster::addListener);

        multicaster.publishEvent(new ApplicationStartingEvent(this.primaryClass, environment));
        envPPs.forEach(pp -> pp.postProcessEnvironment(environment));
        multicaster.publishEvent(new ContextPreparedEvent(this.primaryClass, environment));

        List<DefinitionSource> sources = ServiceLoaderUtils.loadOrdered(DefinitionSource.class);
        List<ComponentDefinition> allDefs = new ArrayList<>();
        for (DefinitionSource src : sources) {
            Collection<ComponentDefinition> defs = src.load(primaryClass, environment);
            if (defs != null && !defs.isEmpty()) allDefs.addAll(defs);
        }

        var registry = new DefinitionRegistry();

        List<BoganRegistryCustomizer> customizers = ServiceLoaderUtils.loadOrdered(BoganRegistryCustomizer.class);
        customizers.addAll(this.registryCustomizers);
        OrderUtils.sort(customizers);
        customizers.forEach(c -> c.customize(registry, environment));

        registry.registerAll(allDefs);

        List<BoganDefinitionPostProcessor> defPPs = ServiceLoaderUtils.loadOrdered(BoganDefinitionPostProcessor.class);
        defPPs.addAll(this.definitionPostProcessors);
        OrderUtils.sort(defPPs);
        defPPs.forEach(pp -> pp.postProcessDefinitions(registry, environment));

        multicaster.publishEvent(new ContextLoadedEvent(this.primaryClass, environment));

        var factory = new ComponentFactory(registry);

        List<BoganFactoryPostProcessor> facPPs = ServiceLoaderUtils.loadOrdered(BoganFactoryPostProcessor.class);
        facPPs.addAll(this.factoryPostProcessors);
        OrderUtils.sort(facPPs);
        facPPs.forEach(pp -> pp.postProcessFactory(factory, environment));

        factory.initialize();
        multicaster.publishEvent(new ContextInitializedEvent(this.primaryClass, environment, factory));
        multicaster.publishEvent(new ContextRefreshedEvent(this.primaryClass, environment, factory));

        ServiceLoaderUtils.loadOrdered(ApplicationRunner.class).forEach(r -> r.run(environment.getArgs()));
        ServiceLoaderUtils.loadOrdered(CommandLineRunner.class).forEach(r -> r.run(environment.getArgs()));

        multicaster.publishEvent(new ApplicationReadyEvent(this.primaryClass, environment, factory));

        DefaultBoganContext context = new DefaultBoganContext(this.primaryClass, environment, factory, multicaster);
        Runtime.getRuntime().addShutdownHook(new Thread(context::close, "bogan-shutdown"));
        return context;
    }
}
