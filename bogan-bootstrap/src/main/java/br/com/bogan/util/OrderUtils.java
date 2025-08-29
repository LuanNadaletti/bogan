package br.com.bogan.util;

import java.util.Comparator;
import java.util.List;

public class OrderUtils {

    private OrderUtils() {
    }

    public static final Comparator<Object> COMPARATOR = (a, b) -> {
        int oa = readOrder(a);
        int ob = readOrder(b);
        if (oa != ob) return Integer.compare(oa, ob);

        String ca = a.getClass().getName();
        String cb = b.getClass().getName();
        return ca.compareTo(cb);
    };

    public static int readOrder(Object o) {
        if (o instanceof Ordered ord) return ord.getOrder();
        Order ann = o.getClass().getAnnotation(Order.class);
        return (ann != null) ? ann.value() : 0;
    }

    public static <T> void sort(List<T> list) {
        list.sort(COMPARATOR);
    }
}
