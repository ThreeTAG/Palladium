package net.threetag.palladium.core.entity;

import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.platform.PlatformHelper;

public class PlayerNameChange {

    public static void refreshDisplayName(Player player) {
        PlatformHelper.PLATFORM.getEntities().refreshDisplayName(player);
    }

}
