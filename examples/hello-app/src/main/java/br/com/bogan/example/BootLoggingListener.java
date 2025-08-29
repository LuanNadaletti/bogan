package br.com.bogan.example;

import br.com.bogan.core.BoganApplicationListener;
import br.com.bogan.core.events.ApplicationReadyEvent;

public class BootLoggingListener implements BoganApplicationListener<ApplicationReadyEvent> {

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("Application ready: " + event.getPrimaryClass().getSimpleName());
    }
}
