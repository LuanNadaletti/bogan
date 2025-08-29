package br.com.bogan.util;

/**
 * Lower value runs first.
 */
public interface Ordered {

    int getOrder();

    Ordered HIGHEST_PRECEDENCE = () -> Integer.MIN_VALUE;
    Ordered LOWEST_PRECEDENCE = () -> Integer.MAX_VALUE;
}
