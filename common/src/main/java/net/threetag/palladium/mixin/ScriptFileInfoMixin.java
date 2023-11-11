package net.threetag.palladium.mixin;

import dev.latvian.mods.kubejs.script.ScriptFileInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(ScriptFileInfo.class)
public interface ScriptFileInfoMixin {

    @Accessor(remap = false)
    Map<String, List<String>>  getProperties();

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

    @Accessor(value = "requiredMods", remap = false)
    Set<String> getRequiredMods();
}
