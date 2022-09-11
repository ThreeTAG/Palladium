package net.threetag.palladium.accessory.forge;

import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.accessory.AccessoryPlayerData;
import net.threetag.palladium.capability.forge.PalladiumCapabilities;

import java.util.Optional;

public class AccessoryImpl {

    public static Optional<AccessoryPlayerData> getPlayerData(Player player) {
        return player.getCapability(PalladiumCapabilities.ACCESSORIES).resolve();
    }

}
