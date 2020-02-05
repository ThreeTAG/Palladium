package net.threetag.threecore.capability;

import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.karma.IKarma;
import net.threetag.threecore.network.KarmaInfoMessage;
import net.threetag.threecore.network.SyncKarmaMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
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

    public static void setKarma(PlayerEntity player, int karma) {
        player.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
            k.setKarma(karma);
            if (player instanceof ServerPlayerEntity)
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncKarmaMessage(player.getEntityId(), k.getKarma()), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });
    }

    public static void addKarma(PlayerEntity player, int karma) {
        addKarma(player, karma, true);
    }

    public static void addKarma(PlayerEntity player, int karma, boolean toast) {
        player.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
            k.setKarma(k.getKarma() + karma);
            if (player instanceof ServerPlayerEntity) {
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncKarmaMessage(player.getEntityId(), k.getKarma()), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);

                if (toast)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new KarmaInfoMessage(karma), ((ServerPlayerEntity) player).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
    }

}
