package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.util.scripts.ScriptParameterName;
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
        this.value.getDataManager().set(data, value);
        return true;
    }

    public boolean isUnlocked() {
        return this.value.getConditionManager().isUnlocked();
    }

    public boolean isEnabled() {
        return this.value.getConditionManager().isEnabled();
    }

    public CompoundNBT getAdditionalNbtData() {
        return this.value.getAdditionalData();
    }

}
