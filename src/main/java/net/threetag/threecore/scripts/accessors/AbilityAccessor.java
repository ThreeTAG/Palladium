package net.threetag.threecore.scripts.accessors;

import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.condition.Condition;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class AbilityAccessor extends ScriptAccessor<Ability> {

    public AbilityAccessor(Ability value) {
        super(value);
    }

    public String getType() {
        return this.value.type.getRegistryName().toString();
    }

    public String getId() {
        return this.value.getId();
    }

    public String getContainerId() {
        return this.value.container.getId().toString();
    }

    public Object getData(@ScriptParameterName("key") String key) {
        ThreeData data = this.value.getDataManager().getDataByName(key);
        return data == null ? null : this.value.getDataManager().get(data);
    }

    public boolean setData(@ScriptParameterName("key") String key, @ScriptParameterName("value") Object value) {
        ThreeData data = this.value.getDataManager().getDataByName(key);
        if (data == null)
            return false;

        // ugly fix since JavaScript numbers are apparently always doubles?
        if (data instanceof IntegerThreeData) {
            if (value instanceof Double)
                value = ((Double) value).intValue();
            else if (value instanceof Float)
                value = ((Float) value).intValue();
        }

        this.value.getDataManager().set(data, value);
        return true;
    }

    public boolean isUnlocked() {
        return this.value.getConditionManager().isUnlocked();
    }

    public boolean isEnabled() {
        return this.value.getConditionManager().isEnabled();
    }

    public CompoundNBTAccessor getAdditionalNbtData() {
        return new CompoundNBTAccessor(this.value.getAdditionalData());
    }

    public ConditionAccessor[] getConditions() {
        Condition[] list = this.value.getConditionManager().getConditions().toArray(new Condition[0]);
        ConditionAccessor[] array = new ConditionAccessor[list.length];
        for (int i = 0; i < list.length; i++)
            array[i] = new ConditionAccessor(list[i]);
        return array;
    }

}
