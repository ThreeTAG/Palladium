package net.threetag.palladium.core.entity;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.player.Player;

public class PlayerNameChange {

    @ExpectPlatform
    public static void refreshDisplayName(Player player) {
        throw new AssertionError();
    }

}
