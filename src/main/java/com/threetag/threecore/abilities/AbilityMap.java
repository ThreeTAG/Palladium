package com.threetag.threecore.abilities;

import java.util.LinkedHashMap;

public class AbilityMap extends LinkedHashMap<String, Ability> {

    @Override
    public Ability put(String key, Ability value) {
        value.id = key;
        return super.put(key, value);
    }

}