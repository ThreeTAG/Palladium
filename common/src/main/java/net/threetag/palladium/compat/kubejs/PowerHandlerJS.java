package net.threetag.palladium.compat.kubejs;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.property.PalladiumProperties;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class PowerHandlerJS {

    private final Player parent;

    public PowerHandlerJS(Player parent) {
        this.parent = parent;
    }

    public Collection<ResourceLocation> getPowers() {
        var handler = PowerManager.getPowerHandler(this.parent);
        return handler.isPresent() ? handler.get().getPowerHolders().keySet() : Collections.emptyList();
    }

    public boolean setSuperpower(ResourceLocation id) {
        Power power = PowerManager.getInstance(Objects.requireNonNull(this.parent).level).getPower(id);

        if (power != null) {
            PalladiumProperties.SUPERPOWER_ID.set(this.parent, id);
            return true;
        } else {
            return false;
        }
    }

    public ResourceLocation getSuperpower() {
        return PalladiumProperties.SUPERPOWER_ID.get(this.parent);
    }

}
