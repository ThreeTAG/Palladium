package net.threetag.threecore.util.scripts.accessors;

import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.util.threedata.ThreeData;

public class AbilityAccessor {

    public final Ability ability;

    public AbilityAccessor(Ability ability) {
        this.ability = ability;
    }

    public String getType() {
        return this.ability.type.getRegistryName().toString();
    }

    public String getId() {
        return this.ability.getId();
    }

    public Object getData(String key) {
        ThreeData data = this.ability.getDataManager().getDataByName(key);
        return data == null ? null : this.ability.getDataManager().get(data);
    }

    public boolean setData(String key, Object value) {
        ThreeData data = this.ability.getDataManager().getDataByName(key);
        if (data == null)
            return false;
        this.ability.getDataManager().set(data, value);
        return true;
    }

    public boolean isUnlocked() {
        return this.ability.getConditionManager().isUnlocked();
    }

    public boolean isEnabled() {
        return this.ability.getConditionManager().isEnabled();
    }

    @Override
    public boolean equals(Object obj) {
        return this.ability.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.ability.hashCode();
    }

}
