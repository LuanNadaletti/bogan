package br.com.bogan.example;

import br.com.bogan.core.Bogan;
import br.com.bogan.core.BoganContext;
import br.com.bogan.factory.ComponentFactory;

public class Main {

    public static void main(String[] args) {
        ComponentFactory componentFactory = Bogan.run(Main.class, args);
        var service = componentFactory.getComponent(Service.class);
        service.execute();

        BoganContext ctx = Bogan.application(Main.class)
                .listeners(new CustomEventListener(), new BootLoggingListener())
                .runContext(args);

        var env = ctx.getEnvironment();
        System.out.println("Profiles ativos: " + env.getActiveProfiles());
        ctx.publishEvent(new MyCustomEvent(Main.class, env));
    }
}
