package net.threetag.palladium.core.entity.fabric;

import net.minecraft.world.entity.player.Player;

public class PlayerNameChangeImpl {

    public static void refreshDisplayName(Player player) {
        if (player instanceof RefreshableDisplayName name) {
            name.palladium$refreshDisplayName();
        }
    }

    public interface RefreshableDisplayName {

        void palladium$refreshDisplayName();

    }

}
