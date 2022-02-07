package net.threetag.palladium.power.ability;

import net.threetag.palladium.power.ability.condition.Condition;
import net.threetag.palladium.util.threedata.ThreeData;
import net.threetag.palladium.util.threedata.ThreeDataEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityConfiguration {

    private final String id;
    private final Ability ability;
    private final Map<ThreeData<?>, ThreeDataEntry<?>> data = new HashMap<>();
    private final List<Condition> unlockingConditions = new ArrayList<>();
    private final List<Condition> enablingConditions = new ArrayList<>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public AbilityConfiguration(String id, Ability ability) {
        this.id = id;
        this.ability = ability;
        ability.defaultData.forEach((d, v) -> this.data.put(d, new ThreeDataEntry(d, v)));
    }

    public String getId() {
        return id;
    }

    public Ability getAbility() {
        return ability;
    }

    public <T> AbilityConfiguration set(ThreeData<T> data, T value) {
        this.data.put(data, new ThreeDataEntry<>(data, value));
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(ThreeData<T> data) {
        return (T) this.data.get(data).getValue();
    }

    public List<Condition> getUnlockingConditions() {
        return unlockingConditions;
    }

    public List<Condition> getEnablingConditions() {
        return enablingConditions;
    }
}
