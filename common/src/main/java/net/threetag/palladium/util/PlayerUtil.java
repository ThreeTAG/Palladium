package net.threetag.palladium.util;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
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
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

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
            return ((AbstractClientPlayer) player).getSkin().model() == PlayerSkin.Model.SLIM;
        return false;
    }

    @Nullable
    public static Input getPlayerInput(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return serverPlayer.getLastClientInput();
        } else if (Platform.getEnvironment() == Env.CLIENT) {
            return getClientsPlayerInput(player);
        } else {
            return null;
        }
    }

    @Environment(EnvType.CLIENT)
    @Nullable
    public static Input getClientsPlayerInput(Player player) {
        var mc = Minecraft.getInstance();

        if (mc.player == player) {
            return new Input(
                    mc.options.keyUp.isDown(),
                    mc.options.keyDown.isDown(),
                    mc.options.keyLeft.isDown(),
                    mc.options.keyRight.isDown(),
                    mc.options.keyJump.isDown(),
                    mc.options.keyShift.isDown(),
                    mc.options.keySprint.isDown()
            );
        } else {
            return null;
        }
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

    public static void playSoundToAll(Level world, double x, double y, double z, double range, ResourceLocation sound, SoundSource category) {
        playSoundToAll(world, x, y, z, range, sound, category, 1, 1);
    }

    public static void playSoundToAll(Level world, double x, double y, double z, double range, ResourceLocation sound, SoundSource category, float volume, float pitch) {
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
