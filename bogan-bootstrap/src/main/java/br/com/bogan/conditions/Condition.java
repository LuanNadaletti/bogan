package br.com.bogan.conditions;

import br.com.bogan.env.BoganEnvironment;

public interface Condition {

    boolean matches(BoganEnvironment env);
}
