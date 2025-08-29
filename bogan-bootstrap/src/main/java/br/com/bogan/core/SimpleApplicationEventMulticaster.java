package br.com.bogan.core;

import br.com.bogan.core.events.BoganEvent;
import br.com.bogan.util.OrderUtils;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleApplicationEventMulticaster {

    private final List<BoganApplicationListener> listeners = new ArrayList<>();

    public void addListener(BoganApplicationListener<?> listener) {
        this.listeners.add(listener);
        OrderUtils.sort(listeners);
    }

    public void removeListener(BoganApplicationListener<?> listener) {
        this.listeners.remove(listener);
    }

    public void publishEvent(BoganEvent event) {
        for (BoganApplicationListener l : listeners) {
            try {
                l.onApplicationEvent(event);
            } catch (ClassCastException ignored) {
                // listener type not matching this event, ignore
            }
        }
    }
}
