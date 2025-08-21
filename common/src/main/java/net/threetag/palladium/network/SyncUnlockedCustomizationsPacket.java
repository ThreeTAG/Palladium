package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.List;
import java.util.stream.Collectors;

public record SyncUnlockedCustomizationsPacket(
        List<ResourceKey<Customization>> unlocked) implements CustomPacketPayload {

    public static final Type<SyncUnlockedCustomizationsPacket> TYPE = new Type<>(Palladium.id("sync_unlocked_customizations"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncUnlockedCustomizationsPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(PalladiumRegistryKeys.CUSTOMIZATION).apply(ByteBufCodecs.list()), SyncUnlockedCustomizationsPacket::unlocked,
            SyncUnlockedCustomizationsPacket::new
    );

    public static void handle(SyncUnlockedCustomizationsPacket packet, NetworkManager.PacketContext context) {
        var handler = EntityCustomizationHandler.get(context.getPlayer());
        handler.setUnlocked(packet.unlocked.stream().map(
                key -> context
                        .registryAccess()
                        .lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION)
                        .get(key).orElseThrow()
        ).collect(Collectors.toUnmodifiableList()));
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
