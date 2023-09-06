package net.threetag.palladium.mixin;

import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ScriptFileInfo.class)
public interface ScriptFileInfoMixin {

    @Accessor(remap = false)
    Map<String, String> getProperties();

    @Accessor(remap = false)
    int getPriority();

    @Accessor(value = "priority", remap = false)
    void setPriority(int priority);

    @Accessor(remap = false)
    boolean getIgnored();

    @Accessor(value = "ignored", remap = false)
    void setIgnored(boolean ignored);

    @Accessor(value = "packMode", remap = false)
    void setPackMode(String packMode);
}
