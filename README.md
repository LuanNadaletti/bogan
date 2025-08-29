# Bogan Framework

Bogan is a lightweight, extensible **IoC/DI container** with a modular bootstrap layer — inspired by concepts from Spring but designed from scratch for learning and experimentation.  
The project is structured to serve as a **core container** with plug-and-play modules for scanning, persistence, web, and beyond.

---

## 📦 Modules

### `bogan-di`
The **core IoC/DI container**:
- Component model: `@Component`, `@Inject`, `@Qualifier`, `@Scope`, `@PostConstruct`, `@PreDestroy`.
- Dependency resolution and injection (constructor, field, provider).
- Scope management (`singleton`, `prototype`).
- Lifecycle callbacks with shutdown hooks.
- Exception hierarchy (`NoSuchComponentException`, `AmbiguousDependencyException`, `CircularDependencyException`).
- Utilities (`Provider<T>`, `ReflectionUtil`, `ComponentNameUtils`).

This module is **pure** — it does not know about SPI, events, or ServiceLoader.

---

### `bogan-bootstrap`
The **bootstrap and lifecycle module**:
- `BoganApplication` orchestrator, similar to `SpringApplication.run`.
- `BoganContext` interface for the running container.
- Event system:
  - `ApplicationStartingEvent`, `ContextPreparedEvent`, `ContextLoadedEvent`,  
    `ContextInitializedEvent`, `ContextRefreshedEvent`, `ApplicationReadyEvent`, `ContextClosedEvent`.
- Environment and profiles (`BoganEnvironment`, `@ConditionalOnProfile`, `@ConditionalOnProperty`, `@ConditionalOnClass`).
- Extension points (SPIs):
  - `DefinitionSource` (load component definitions from any origin).
  - `BoganRegistryCustomizer`, `BoganDefinitionPostProcessor`, `BoganFactoryPostProcessor`.
  - `EnvironmentPostProcessor`.
  - `BoganApplicationListener`.
  - `ApplicationRunner` / `CommandLineRunner`.

---

### `bogan-scan`
A **pluggable component scanner**:
- Implements `DefinitionSource` via `SnifferDefinitionSource`.
- Uses [ClassGraph](https://github.com/classgraph/classgraph) to discover annotated components on the classpath.
- Exposed via `META-INF/services/br.com.bogan.core.DefinitionSource`, so the bootstrap can pick it up automatically.

---

### `examples/hello-app`
A **minimal demo application** that shows how to bootstrap and run the framework.

```java
package com.example;

import br.com.bogan.core.Bogan;
import br.com.bogan.factory.ComponentFactory;

public class MyApp {
    public static void main(String[] args) {
        ComponentFactory factory = Bogan.run(MyApp.class, args);
        MyService service = factory.getComponent(MyService.class);
        service.execute();
    }
}
```

---

## 🚀 Getting Started

### Build
Requires **JDK 21** and **Gradle 8.9+**.

```bash
./gradlew clean build
```

### Run the example
```bash
./gradlew :examples:hello-app:run
```

---

## 🔧 Concepts

- **Core (`bogan-di`)**: stable kernel, only knows how to register and create components.
- **Bootstrap (`bogan-bootstrap`)**: orchestrates lifecycle, environment, events, SPI discovery.
- **Scan (`bogan-scan`)**: an optional module that implements `DefinitionSource` using ClassGraph.
- **Profiles**: allow selective activation of components depending on the environment:
  ```java
  @Component
  @ConditionalOnProfile("dev")
  class DevDatabaseConfig {}
  ```

---

## 🧩 Extension Points

You can extend the framework by implementing and registering providers via `META-INF/services`:

- `DefinitionSource` → add new ways of loading components (YAML, remote, etc.).
- `EnvironmentPostProcessor` → enrich environment before beans load.
- `BoganRegistryCustomizer` → customize the definition registry.
- `BoganFactoryPostProcessor` → customize the component factory.
- `BoganApplicationListener` → listen to lifecycle events.
- `ApplicationRunner` / `CommandLineRunner` → run code after startup.

---

## 📌 Roadmap

- **Persistence module (`bogan-persistence`)**: integrate a DataSource and transaction management.
- **Web module (`bogan-web`)**: lightweight HTTP server + routing.
- **Config module (`bogan-config`)**: load configuration from YAML/JSON into the environment.
- **Improved DX**: bootstrap report (profiles, env sources, discovered SPIs, number of definitions).

---

## 🙋 Motivation

This project started as a **learning experiment** to understand deeply how DI/IoC containers and bootstrapping frameworks like Spring work. The idea is to **start with a solid DI kernel** and **grow new modules** (persistence, web, config, …) on top, while keeping the architecture **clean and modular**.
