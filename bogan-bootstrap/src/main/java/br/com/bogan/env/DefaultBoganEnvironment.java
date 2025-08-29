package br.com.bogan.env;

import java.util.*;

public final class DefaultBoganEnvironment implements BoganEnvironment {

    private final List<PropertySource> sources = new ArrayList<>();
    private final Properties defaults = new Properties();
    private final List<String> profiles = new ArrayList<>();
    private String[] args = new String[0];

    @Override
    public String getProperty(String key) {
        for (PropertySource s : sources) {
            String v = s.getProperty(key);
            if (v != null) return v;
        }

        return defaults.getProperty(key);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        String v = getProperty(key);

        return v != null ? v : defaultValue;
    }

    @Override
    public boolean containsProperty(String key) {
        return getProperty(key) != null;
    }

    @Override
    public List<String> getActiveProfiles() {
        return Collections.unmodifiableList(profiles);
    }

    @Override
    public void setActiveProfiles(List<String> profiles) {
        profiles.clear();
        this.profiles.addAll(profiles);
    }

    @Override
    public void addPropertySource(PropertySource s) {
        sources.add(Objects.requireNonNull(s));
    }

    @Override
    public void addDefaultProperties(Properties p) {
        defaults.putAll(p);
    }

    @Override
    public void applyArgs(String... a) {
        args = (a == null) ? new String[0] : a.clone();
        Map<String, String> map = new LinkedHashMap<>();

        if (a != null) {
            for (String s : a) {
                if (s == null) continue;

                String str = s.startsWith("--") ? s.substring(2) : s;
                int idx = str.indexOf('=');
                if (idx > 0) map.put(str.substring(0, idx), str.substring(idx + 1));
            }
        }
        if (!map.isEmpty()) addPropertySource(new MapPropertySource("args", map));
    }

    @Override
    public String[] getArgs() {
        return args.clone();
    }
}
