package com.threetag.threecore.karma.capability;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.karma.network.MessageKarmaInfo;
import com.threetag.threecore.karma.network.MessageSyncKarma;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.network.NetworkDirection;

public class CapabilityKarma implements IKarma {

    @CapabilityInject(IKarma.class)
    public static Capability<IKarma> KARMA;

    public static final int MAX = 1000;
    public static final int MIN = -1000;

    protected int karma;

    @Override
    public int getKarma() {
        return MathHelper.clamp(this.karma, MIN, MAX);
    }

    @Override
    public void setKarma(int karma) {
        this.karma = MathHelper.clamp(karma, MIN, MAX);
    }

    public static void setKarma(EntityPlayer player, int karma) {
        player.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
            k.setKarma(karma);
            if (player instanceof EntityPlayerMP)
                ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSyncKarma(player.getEntityId(), k.getKarma()), ((EntityPlayerMP) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });
    }

    public static void addKarma(EntityPlayer player, int karma) {
        addKarma(player, karma, true);
    }

    public static void addKarma(EntityPlayer player, int karma, boolean toast) {
        player.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
            k.setKarma(k.getKarma() + karma);
            if (player instanceof EntityPlayerMP) {
                ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSyncKarma(player.getEntityId(), k.getKarma()), ((EntityPlayerMP) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);

                if (toast)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new MessageKarmaInfo(karma), ((EntityPlayerMP) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
    }

}
