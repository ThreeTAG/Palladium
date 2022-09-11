package net.threetag.palladium.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

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

    public static void playSound(Player player, double x, double y, double z, SoundEvent sound, SoundSource category) {
        playSound(player, x, y, z, sound, category, 1F, 1F);
    }

    public static void playSound(Player player, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch) {
        if (player instanceof ServerPlayer) {
            ((ServerPlayer) player).connection.send(new ClientboundCustomSoundPacket(Objects.requireNonNull(Registry.SOUND_EVENT.getKey(sound)), category, new Vec3(x, y, z), volume, pitch, player.getRandom().nextLong()));
        }
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, SoundEvent sound, SoundSource category) {
        playSoundToAll(world, x, y, z, range, sound, category, 1, 1);
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, SoundEvent sound, SoundSource category, float volume, float pitch) {
        AABB a = new AABB(new BlockPos(x - range, y - range, z - range), new BlockPos(x + range, y + range, z + range));
        for (Player players : world.getEntitiesOfClass(Player.class, a)) {
            playSound(players, x, y, z, sound, category, volume, pitch);
        }
    }
}
