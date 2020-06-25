package net.threetag.threecore.scripts.events;

import net.minecraft.entity.Entity;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.IntegerThreeData;
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

    // ugly fix since JavaScript numbers are apparently always doubles?

    public void registerInteger(@ScriptParameterName("data") IntegerThreeData data, @ScriptParameterName("defaultValue") double defaultValue) {
        this.threeData.register(data, new Double(defaultValue).intValue());
    }

    public void registerFloat(@ScriptParameterName("data") FloatThreeData data, @ScriptParameterName("defaultValue") double defaultValue) {
        this.threeData.register(data, new Double(defaultValue).floatValue());
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
