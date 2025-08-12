package br.com.bogan.example;

import br.com.bogan.annotations.Component;
import br.com.bogan.annotations.Inject;
import br.com.bogan.annotations.PostConstruct;
import br.com.bogan.annotations.ScanPoint;
import br.com.bogan.core.Bogan;

@Component
public class ExampleClass {

    public static void main(String[] args) {
        Bogan.run(ExampleClass.class, args);
    }

    @ScanPoint
    @Component
    class AppRoot {
        private final GreetingService svc;
        @Inject AppRoot(GreetingService svc) { this.svc = svc; }

        @PostConstruct
        void start() {
            svc.sayHello();
        }
    }

    @Component
    static class GreetingService {
        private final MessageProvider provider;

        @Inject
        GreetingService(MessageProvider provider) {
            this.provider = provider;
        }

        void sayHello() {
            System.out.println(provider.getMessage());
        }
    }

    @Component
    static class MessageProvider {
        String getMessage() {
            return "Hello from Bogan!";
        }
    }
}
