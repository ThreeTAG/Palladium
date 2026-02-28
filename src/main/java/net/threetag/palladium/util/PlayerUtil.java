package net.threetag.palladium.util;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.flight.EntityFlightHandler;

public class PlayerUtil {

    public static boolean isFlying(LivingEntity entity) {
        return isCreativeFlying(entity) || EntityFlightHandler.get(entity).isFlying();
    }

    public static boolean isCreativeFlying(LivingEntity entity) {
        if (entity instanceof Player) {
            return ((Player) entity).getAbilities().flying;
        } else {
            return false;
        }
    }

    public static Input getMovementInput(Player player) {
        return Palladium.PROXY.getMovementInput(player);
    }

    public static boolean hasSmallArms(Player player) {
        return Palladium.PROXY.playerHasSlimModel(player);
    }

    public static void playSound(Player player, double x, double y, double z, SoundEvent sound, SoundSource soundSource) {
        playSound(player, x, y, z, sound, soundSource, 1F, 1F);
    }

    public static void playSound(Player player, double x, double y, double z, SoundEvent sound, SoundSource soundSource, float volume, float pitch) {
        if (player instanceof ServerPlayer) {
            ((ServerPlayer) player).connection.send(new ClientboundSoundPacket(Holder.direct(sound), soundSource, x, y, z, volume, pitch, player.getRandom().nextLong()));
        }
    }

    public static void playSound(Player player, double x, double y, double z, Identifier sound, SoundSource category) {
        playSound(player, x, y, z, sound, category, 1F, 1F);
    }

    public static void playSound(Player player, double x, double y, double z, Identifier sound, SoundSource category, float volume, float pitch) {
        if (player instanceof ServerPlayer serverPlayer) {
            BuiltInRegistries.SOUND_EVENT.get(sound).ifPresent(ref -> serverPlayer.connection.send(new ClientboundSoundPacket(ref, category, x, y, z, volume, pitch, player.getRandom().nextLong())));
        }
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, SoundEvent sound, SoundSource category) {
        playSoundToAll(world, x, y, z, range, sound, category, 1, 1);
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, SoundEvent sound, SoundSource category, float volume, float pitch) {
        AABB a = new AABB(new Vec3(x - range, y - range, z - range), new Vec3(x + range, y + range, z + range));
        for (Player players : world.getEntitiesOfClass(Player.class, a)) {
            playSound(players, x, y, z, sound, category, volume, pitch);
        }
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, Identifier sound, SoundSource category) {
        playSoundToAll(world, x, y, z, range, sound, category, 1, 1);
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, Identifier sound, SoundSource category, float volume, float pitch) {
        AABB a = new AABB(new Vec3(x - range, y - range, z - range), new Vec3(x + range, y + range, z + range));
        for (Player players : world.getEntitiesOfClass(Player.class, a)) {
            playSound(players, x, y, z, sound, category, volume, pitch);
        }
    }

    public static <T extends ParticleOptions> void spawnParticle(Player player, T particleIn, boolean longDistanceIn, double xIn, double yIn, double zIn, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speedIn, int countIn) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundLevelParticlesPacket(particleIn, false, longDistanceIn, xIn, yIn, zIn, xOffsetIn, yOffsetIn, zOffsetIn, speedIn, countIn));
        }
    }

    public static <T extends ParticleOptions> void spawnParticleForAll(Level world, double range, T particleIn, boolean longDistanceIn, double xIn, double yIn, double zIn, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speedIn, int countIn) {
        AABB a = new AABB(new Vec3(xIn - range, yIn - range, zIn - range), new Vec3(xIn + range, yIn + range, zIn + range));
        for (Player players : world.getEntitiesOfClass(Player.class, a)) {
            spawnParticle(players, particleIn, longDistanceIn, xIn, yIn, zIn, xOffsetIn, yOffsetIn, zOffsetIn, speedIn, countIn);
        }
    }
}
