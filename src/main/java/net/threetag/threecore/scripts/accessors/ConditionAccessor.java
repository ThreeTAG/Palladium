package net.threetag.threecore.scripts.accessors;

import net.threetag.threecore.ability.condition.Condition;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.threedata.ThreeData;

public class ConditionAccessor extends ScriptAccessor<Condition> {

    private final AbilityAccessor abilityAccessor;

    public ConditionAccessor(Condition value) {
        super(value);
        this.abilityAccessor = new AbilityAccessor(value.ability);
    }

    public String getType() {
        return this.value.type.getRegistryName().toString();
    }

    public Object getData(@ScriptParameterName("key") String key) {
        ThreeData data = this.value.getDataManager().getDataByName(key);
        return data == null ? null : this.value.getDataManager().get(data);
    }

    public boolean setData(@ScriptParameterName("key") String key, @ScriptParameterName("value") Object value) {
        ThreeData data = this.value.getDataManager().getDataByName(key);
        if (data == null)
            return false;
        this.value.getDataManager().set(data, value);
        return true;
    }

    public boolean isEnabling() {
        return this.value.getDataManager().get(Condition.ENABLING);
    }

    public boolean isInverted() {
        return this.value.getDataManager().get(Condition.INVERT);
    }

    public AbilityAccessor getAbility() {
        return abilityAccessor;
    }
}
