package br.com.bogan.env;

import java.util.List;
import java.util.Properties;

public interface BoganEnvironment {

    String getProperty(String key);
    String getProperty(String key, String defaultValue);
    boolean containsProperty(String key);

    List<String> getActiveProfiles();
    void setActiveProfiles(List<String> profiles);

    void addPropertySource(PropertySource source);
    void addDefaultProperties(Properties defaults);

    /** Store raw args for runners; also parses "--k=v" into a PropertySource. */
    void applyArgs(String... args);

    /** Return raw args passed to run. */
    String[] getArgs();
}
