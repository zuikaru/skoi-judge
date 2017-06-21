package com.talesdev.judge;

/**
 * Utilities for removing extension from file name
 * Created by MoKunz on 1/2/2017.
 */
public final class NoExtension {
    private final String name;
    private static final char SEPARATOR = '.';
    public NoExtension(String name) {
        this.name = name;
    }

    public String name(){
        if(name == null) return "";
        int pos = name.lastIndexOf(SEPARATOR);
        return name.substring(0,pos);
    }
}
