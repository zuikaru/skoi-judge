package com.talesdev.judge;

import java.util.HashMap;
import java.util.Map;

/**
 * Config map
 * Created by MoKunz on 1/3/2017.
 */
public final class ConfigMap{
    private final Key[] list;

    public ConfigMap(Key... keys) {
        this.list = keys;
    }

    public Map<String,String> map(){
        Map<String,String> m = new HashMap<>();
        for(Key k : list) m.put(k.key(),k.value());
        return m;
    }
}
