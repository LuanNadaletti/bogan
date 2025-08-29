package br.com.bogan.example;

import br.com.bogan.core.BoganApplicationListener;

public class CustomEventListener implements BoganApplicationListener<MyCustomEvent> {

    @Override
    public void onApplicationEvent(MyCustomEvent event) {
        System.out.println("Custom event triggered " + event.getPrimaryClass().getSimpleName());
    }
}
