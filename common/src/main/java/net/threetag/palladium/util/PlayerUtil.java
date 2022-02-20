package net.threetag.palladium.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class PlayerUtil {

    public static boolean isCreativeFlying(LivingEntity entity) {
        if (entity instanceof Player) {
            return ((Player) entity).getAbilities().flying;
        } else {
            return false;
        }
    }

    @Environment(EnvType.CLIENT)
    public static boolean hasSmallArms(Player player) {
        if (player instanceof AbstractClientPlayer)
            return ((AbstractClientPlayer) player).getModelName().equalsIgnoreCase("slim");
        return false;
    }

//    public static Set<ServerPlayer> getPlayersTracking(Entity e) {
//        ChunkStorage
//        ThreadedAnvilChunkStorage tacs = ((ServerChunkCache) e.getLevel().getChunkSource()).thre;
//        return tacs.entityTrackers.get(e.getEntityId()).playersTracking;
//    }
}
