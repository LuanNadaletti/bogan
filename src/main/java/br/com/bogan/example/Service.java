package br.com.bogan.example;

import br.com.bogan.annotations.Component;
import br.com.bogan.annotations.Inject;

@Component
public class Service {

    private final Repo repo;

    @Inject
    public Service(Repo repo) {
        this.repo = repo;
    }

    void init() {

    }

    public Repo repo() {
        return repo;
    }
}
