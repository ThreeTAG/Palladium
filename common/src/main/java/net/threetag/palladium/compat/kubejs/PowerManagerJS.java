package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.level.LevelJS;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;

import java.util.Collection;
import java.util.stream.Collectors;

public class PowerManagerJS {

    private final LevelJS parent;

    public PowerManagerJS(LevelJS parent) {
        this.parent = parent;
    }

    public Collection<ResourceLocation> getPowers() {
        return PowerManager.getInstance(this.parent.minecraftLevel).getPowers().stream().map(Power::getId).collect(Collectors.toList());
    }

}
