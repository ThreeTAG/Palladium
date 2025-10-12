package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilityReference;

public record SyncAbilityComponentPacket(int entityId, AbilityReference reference,
                                         DataComponentPatch patch) implements CustomPacketPayload {

    public static final Type<SyncAbilityComponentPacket> TYPE = new Type<>(Palladium.id("sync_ability_component"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAbilityComponentPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncAbilityComponentPacket::entityId,
            AbilityReference.STREAM_CODEC, SyncAbilityComponentPacket::reference,
            DataComponentPatch.STREAM_CODEC, SyncAbilityComponentPacket::patch,
            SyncAbilityComponentPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncAbilityComponentPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> Palladium.PROXY.packetHandleSyncAbilityComponent(packet, context));
    }

}
