package br.com.bogan.env;

import br.com.bogan.util.Ordered;

public interface EnvironmentPostProcessor extends Ordered {
    void postProcessEnvironment(BoganEnvironment environment);
}
