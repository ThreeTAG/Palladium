package net.threetag.palladium.entity.data;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface PalladiumPlayerDataType<T extends PalladiumEntityData<? extends Player>> extends PalladiumEntityDataType<T> {

    T makeForPlayer(Player player);

    @Override
    default T make(Entity entity) {
        if (entity instanceof Player player) {
            return this.makeForPlayer(player);
        } else {
            return null;
        }
    }
}
