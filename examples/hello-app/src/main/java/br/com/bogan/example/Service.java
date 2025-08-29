package br.com.bogan.example;

import br.com.bogan.annotations.Component;

@Component
public final class Service {

    public void execute() {
        System.out.println("Service Executando...");
    }
}
