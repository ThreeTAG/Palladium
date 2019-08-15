package com.threetag.threecore.util.scripts.accessors;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.data.ThreeData;

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
        ThreeData data = this.ability.getDataManager().getAbilityDataByName(key);
        return data == null ? null : this.ability.getDataManager().get(data);
    }

    public boolean setData(String key, Object value) {
        ThreeData data = this.ability.getDataManager().getAbilityDataByName(key);
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

}
