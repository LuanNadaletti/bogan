package br.com.bogan.scan;

import br.com.bogan.annotations.Component;
import br.com.bogan.annotations.Inject;
import br.com.bogan.annotations.ScanPoint;

@ScanPoint
@Component
public class Service {

    @Component
    static class Dep {
    }

    @Component
    static class Svc {
        @Inject
        Svc(Dep d) {
        }
    }
}
