package net.threetag.palladium.neoforge.platform;

import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.platform.EntityService;

public class NeoForgeEntities implements EntityService {

    @Override
    public void refreshDisplayName(Player player) {
        player.refreshDisplayName();
    }

}
