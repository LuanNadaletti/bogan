package br.com.bogan.env;

public interface PropertySource {

    String getName();
    String getProperty(String key);
}
