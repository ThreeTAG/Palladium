package net.threetag.threecore.scripts.events;

import net.minecraft.entity.Entity;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.ThreeData;

public class RegisterEntityThreeDataScriptEvent extends EntityScriptEvent {

    private final IThreeDataHolder threeData;

    public RegisterEntityThreeDataScriptEvent(Entity entity, IThreeDataHolder threeData) {
        super(entity);
        this.threeData = threeData;
    }

    public <T> void register(@ScriptParameterName("data") ThreeData<T> data, @ScriptParameterName("defaultValue") T defaultValue) {
        this.threeData.register(data, defaultValue);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
