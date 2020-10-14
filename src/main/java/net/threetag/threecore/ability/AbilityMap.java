package net.threetag.threecore.ability;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

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

    public AbilityMap(List<Supplier<Ability>> abilityGenerators) {
        if (abilityGenerators != null)
            abilityGenerators.forEach(a -> {
                Ability ability = a.get();
                this.put(ability.getId(), ability);
            });
    }

    @Override
    public Ability put(String key, Ability value) {
        if (value == null)
            return null;
        return super.put(key, value);
    }

}
