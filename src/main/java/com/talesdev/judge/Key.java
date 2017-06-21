package com.talesdev.judge;

/**
 * Key
 * Created by MoKunz on 1/29/2017.
 */
public class Key {
    private final String key;
    private final String value;

    public Key(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }
}
