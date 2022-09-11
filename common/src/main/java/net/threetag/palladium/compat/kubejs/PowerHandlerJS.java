package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.player.PlayerDataJS;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.property.PalladiumProperties;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class PowerHandlerJS {

    private final PlayerDataJS<?, ?> parent;

    public PowerHandlerJS(PlayerDataJS<?, ?> parent) {
        this.parent = parent;
    }

    public Collection<ResourceLocation> getPowers() {
        var handler = PowerManager.getPowerHandler(this.parent.getMinecraftPlayer());
        return handler.isPresent() ? handler.get().getPowerHolders().keySet() : Collections.emptyList();
    }

    public boolean setSuperpower(ResourceLocation id) {
        Power power = PowerManager.getInstance(Objects.requireNonNull(this.parent.getMinecraftPlayer()).level).getPower(id);

        if (power != null) {
            PalladiumProperties.SUPERPOWER_ID.set(this.parent.getMinecraftPlayer(), id);
            return true;
        } else {
            return false;
        }
    }

    public ResourceLocation getSuperpower() {
        return PalladiumProperties.SUPERPOWER_ID.get(this.parent.getMinecraftPlayer());
    }

}
