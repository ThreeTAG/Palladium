package com.threetag.threecore.util.helper;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PlayerHelper {

    public static void playSound(EntityPlayer player, double x, double y, double z, SoundEvent sound, SoundCategory category) {
        playSound(player, x, y, z, sound, category, 1F, 1F);
    }

    public static void playSound(EntityPlayer player, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketCustomSound(sound.getRegistryName(), category, new Vec3d(x, y, z), volume, pitch));
        }
    }

    public static void playSoundToAll(World world, double x, double y, double z, double range, SoundEvent sound, SoundCategory category) {
        playSoundToAll(world, x, y, z, range, sound, category, 1, 1);
    }

    public static void playSoundToAll(World world, double x, double y, double z, double range, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        AxisAlignedBB a = new AxisAlignedBB(new BlockPos(x - range, y - range, z - range), new BlockPos(x + range, y + range, z + range));
        for (EntityPlayer players : world.getEntitiesWithinAABB(EntityPlayer.class, a)) {
            playSound(players, x, y, z, sound, category, volume, pitch);
        }
    }

    public static void spawnParticle(EntityPlayer player, IParticleData particleIn, boolean longDistanceIn, float xIn, float yIn, float zIn, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speedIn, int countIn) {
        if (player instanceof EntityPlayerMP) {
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketParticles(particleIn, longDistanceIn, xIn, yIn, zIn, xOffsetIn, yOffsetIn, zOffsetIn, speedIn, countIn));
        }
    }

    public static void spawnParticleForAll(World world, double range, IParticleData particleIn, boolean longDistanceIn, float xIn, float yIn, float zIn, float xOffsetIn, float yOffsetIn, float zOffsetIn, float speedIn, int countIn) {
        AxisAlignedBB a = new AxisAlignedBB(new BlockPos(xIn - range, yIn - range, zIn - range), new BlockPos(xIn + range, yIn + range, zIn + range));
        for (EntityPlayer players : world.getEntitiesWithinAABB(EntityPlayer.class, a)) {
            spawnParticle(players, particleIn, longDistanceIn, xIn, yIn, zIn, xOffsetIn, yOffsetIn, zOffsetIn, speedIn, countIn);
        }
    }

}
