package br.com.bogan.resolve;

import br.com.bogan.factory.ComponentFactory;

import java.util.ArrayDeque;
import java.util.Deque;

public class ResolverContext {

    private final ComponentFactory factory;
    private final ThreadLocal<Deque<String>> stackTL = ThreadLocal.withInitial(ArrayDeque::new);

    public ResolverContext(ComponentFactory factory) {
        this.factory = factory;
    }

    public ComponentFactory factory() {
        return this.factory;
    }

    public void push(String name) {
        stackTL.get().push(name);
    }

    public void pop() {
        stackTL.get().pop();
    }

    public boolean contains(String name) {
        return stackTL.get().contains(name);
    }

    public Deque<String> snapshot() {
        return new ArrayDeque<>(stackTL.get());
    }
}
