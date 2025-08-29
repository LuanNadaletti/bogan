package br.com.bogan.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ServiceLoaderUtils {

    private ServiceLoaderUtils() {
    }

    public static <T> List<T> load(Class<T> type) {
        List<T> list = new ArrayList<>();
        for  (T t : ServiceLoader.load(type)) {
            list.add(t);
        }
        return list;
    }

    public static <T> List<T> loadOrdered(Class<T> type) {
        List<T> list = load(type);
        OrderUtils.sort(list);
        return list;
    }
}
