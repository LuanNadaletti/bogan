package br.com.bogan.factory;

import br.com.bogan.util.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.Deque;

public class LifecycleManager {

    private final Deque<Object> components;

    public LifecycleManager(Deque<Object> components) {
        this.components = components;
    }

    public void onSingletonCreated(String name, Object instance) {
        components.addLast(instance);
    }

    public void destroyAll() {
        while (!components.isEmpty()) {
            Object component = components.pop();
            invokePreDestroy(component);
            invokeAutoCloseable(component);
        }
    }

    private void invokePreDestroy(Object component) {
        Method preDestroy = ReflectionUtil.getPreDestroyMethod(component.getClass()).orElse(null);

        if (preDestroy == null) {
            return;
        }

        ReflectionUtil.executeMethod(preDestroy, component);
    }

    private void invokeAutoCloseable(Object component) {

    }
}
