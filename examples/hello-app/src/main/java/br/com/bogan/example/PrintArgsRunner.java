package br.com.bogan.example;

import br.com.bogan.core.ApplicationRunner;

public class PrintArgsRunner implements ApplicationRunner {

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void run(String[] args) {
        System.out.println("Args recebidos: " + String.join(" ", args));
    }
}
