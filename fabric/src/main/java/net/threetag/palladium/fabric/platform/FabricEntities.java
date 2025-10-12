package net.threetag.palladium.fabric.platform;

import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.platform.EntityService;

public class FabricEntities implements EntityService {

    @Override
    public void refreshDisplayName(Player player) {
        if (player instanceof RefreshableDisplayName name) {
            name.palladium$refreshDisplayName();
        }
    }

    public interface RefreshableDisplayName {

        void palladium$refreshDisplayName();

    }
}
