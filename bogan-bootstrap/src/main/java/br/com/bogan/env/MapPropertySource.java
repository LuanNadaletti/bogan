package br.com.bogan.env;

import java.util.Map;
import java.util.Objects;

public class MapPropertySource implements PropertySource {

    private final String name;
    private final Map<String, String> map;

    public MapPropertySource(String name, Map<String, String> map) {
        this.name = Objects.requireNonNull(name);
        this.map = Objects.requireNonNull(map);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getProperty(String key) {
        return "";
    }
}
