package net.threetag.palladium.compat.kubejs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;

import java.util.Collection;
import java.util.stream.Collectors;

public class PowerManagerJS {

    private final Level parent;

    public PowerManagerJS(Level parent) {
        this.parent = parent;
    }

    public Collection<ResourceLocation> getPowers() {
        return PowerManager.getInstance(this.parent).getPowers().stream().map(Power::getId).collect(Collectors.toList());
    }

}
