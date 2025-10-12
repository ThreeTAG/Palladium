package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record SyncEntityCustomizationsPacket(int entityId,
                                             List<Holder<Customization>> selected) implements CustomPacketPayload {

    public static final Type<SyncEntityCustomizationsPacket> TYPE = new Type<>(Palladium.id("sync_entity_customizations"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityCustomizationsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncEntityCustomizationsPacket::entityId,
            ByteBufCodecs.holderRegistry(PalladiumRegistryKeys.CUSTOMIZATION).apply(ByteBufCodecs.list()), SyncEntityCustomizationsPacket::selected,
            SyncEntityCustomizationsPacket::new
    );

    @Override
    public @NotNull Type<SyncEntityCustomizationsPacket> type() {
        return TYPE;
    }

    public static void handle(SyncEntityCustomizationsPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> Palladium.PROXY.packetHandleSyncEntityCustomizations(packet, context));
    }

    public static SyncEntityCustomizationsPacket create(LivingEntity livingEntity) {
        var handler = EntityCustomizationHandler.get(livingEntity);
        return new SyncEntityCustomizationsPacket(livingEntity.getId(), handler.getSelected().stream().toList());
    }
}
