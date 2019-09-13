package net.threetag.threecore.abilities;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AbilityMap extends LinkedHashMap<String, Ability> {

    public AbilityMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public AbilityMap(int initialCapacity) {
        super(initialCapacity);
    }

    public AbilityMap() {
    }

    public AbilityMap(Map<? extends String, ? extends Ability> m) {
        super(m);
    }

    public AbilityMap(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }

    public AbilityMap(List<AbilityGenerator> abilityGenerators) {
        abilityGenerators.forEach(a -> {
            this.put(a.key, a.create());
        });
    }

    @Override
    public Ability put(String key, Ability value) {
        if (value == null)
            return null;
        value.id = key;
        return super.put(key, value);
    }

}