package net.threetag.palladium.accessory.fabric;

import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.accessory.AccessoryPlayerData;
import net.threetag.palladium.components.fabric.PalladiumComponents;

import java.util.Optional;

public class AccessoryImpl {

    public static Optional<AccessoryPlayerData> getPlayerData(Player player) {
        try {
            return Optional.of(PalladiumComponents.ACCESSORIES.get(player));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
