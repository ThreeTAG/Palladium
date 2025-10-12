package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.NotNull;

public record SyncEntityCustomizationPacket(int entityId,
                                            Holder<Customization> customization) implements CustomPacketPayload {

    public static final Type<SyncEntityCustomizationPacket> TYPE = new Type<>(Palladium.id("sync_entity_customization"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityCustomizationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncEntityCustomizationPacket::entityId,
            ByteBufCodecs.holderRegistry(PalladiumRegistryKeys.CUSTOMIZATION), SyncEntityCustomizationPacket::customization,
            SyncEntityCustomizationPacket::new
    );

    @Override
    public @NotNull Type<SyncEntityCustomizationPacket> type() {
        return TYPE;
    }

    public static void handle(SyncEntityCustomizationPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> Palladium.PROXY.packetHandleSyncEntityCustomization(packet, context));
    }
}
