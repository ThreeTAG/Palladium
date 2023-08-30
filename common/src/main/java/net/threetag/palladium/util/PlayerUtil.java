package net.threetag.palladium.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

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

    public static void playSound(Player player, double x, double y, double z, SoundEvent sound, SoundSource soundSource) {
        playSound(player, x, y, z, sound, soundSource, 1F, 1F);
    }

    public static void playSound(Player player, double x, double y, double z, SoundEvent sound, SoundSource soundSource, float volume, float pitch) {
        if (player instanceof ServerPlayer) {
            ((ServerPlayer) player).connection.send(new ClientboundSoundPacket(Holder.direct(sound), soundSource, x, y, z, volume, pitch, player.getRandom().nextLong()));
        }
    }

    public static void playSound(Player player, double x, double y, double z, ResourceLocation sound, SoundSource category) {
        playSound(player, x, y, z, sound, category, 1F, 1F);
    }

    public static void playSound(Player player, double x, double y, double z, ResourceLocation sound, SoundSource category, float volume, float pitch) {
        if (player instanceof ServerPlayer) {
            ((ServerPlayer) player).connection.send(new ClientboundSoundPacket(Holder.direct(BuiltInRegistries.SOUND_EVENT.get(sound)), category, x, y, z, volume, pitch, player.getRandom().nextLong()));
        }
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, SoundEvent sound, SoundSource category) {
        playSoundToAll(world, x, y, z, range, sound, category, 1, 1);
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, SoundEvent sound, SoundSource category, float volume, float pitch) {
        AABB a = new AABB(BlockPos.containing(x - range, y - range, z - range), BlockPos.containing(x + range, y + range, z + range));
        for (Player players : world.getEntitiesOfClass(Player.class, a)) {
            playSound(players, x, y, z, sound, category, volume, pitch);
        }
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, ResourceLocation sound, SoundSource category) {
        playSoundToAll(world, x, y, z, range, sound, category, 1, 1);
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, ResourceLocation sound, SoundSource category, float volume, float pitch) {
        AABB a = new AABB(BlockPos.containing(x - range, y - range, z - range), BlockPos.containing(x + range, y + range, z + range));
        for (Player players : world.getEntitiesOfClass(Player.class, a)) {
            playSound(players, x, y, z, sound, category, volume, pitch);
        }
    }

    public static <T extends ParticleOptions> void spawnParticle(Player player, T particleIn, boolean longDistanceIn, double xIn, double yIn, double zIn, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speedIn, int countIn) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundLevelParticlesPacket(particleIn, longDistanceIn, xIn, yIn, zIn, xOffsetIn, yOffsetIn, zOffsetIn, speedIn, countIn));
        }
    }

    public static <T extends ParticleOptions> void spawnParticleForAll(Level world, double range, T particleIn, boolean longDistanceIn, double xIn, double yIn, double zIn, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speedIn, int countIn) {
        AABB a = new AABB(BlockPos.containing(xIn - range, yIn - range, zIn - range), BlockPos.containing(xIn + range, yIn + range, zIn + range));
        for (Player players : world.getEntitiesOfClass(Player.class, a)) {
            spawnParticle(players, particleIn, longDistanceIn, xIn, yIn, zIn, xOffsetIn, yOffsetIn, zOffsetIn, speedIn, countIn);
        }
    }
}
