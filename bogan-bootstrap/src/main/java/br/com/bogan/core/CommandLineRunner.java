package br.com.bogan.core;

import br.com.bogan.util.Ordered;

public interface CommandLineRunner extends Ordered {

    void run(String[] args);
}
