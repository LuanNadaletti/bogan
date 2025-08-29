package br.com.bogan.core;

import br.com.bogan.core.events.BoganEvent;

/**
 * Generic application listener (ordered via OrderUtils by interface or @Order).
 */
public interface BoganApplicationListener<E extends BoganEvent> {

    void onApplicationEvent(E event);
}
