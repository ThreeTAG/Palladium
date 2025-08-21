package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public record SelectCustomizationPacket(ResourceKey<Customization> customization) implements CustomPacketPayload {

    public static final Type<SelectCustomizationPacket> TYPE = new Type<>(Palladium.id("select_customization"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SelectCustomizationPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(PalladiumRegistryKeys.CUSTOMIZATION), SelectCustomizationPacket::customization,
            SelectCustomizationPacket::new
    );

    public static void handle(SelectCustomizationPacket packet, NetworkManager.PacketContext context) {
        var handler = EntityCustomizationHandler.get(context.getPlayer());
        context.registryAccess().get(packet.customization).ifPresent(accessory -> {
            if (handler.isSelected(accessory)) {
                handler.unselect(accessory.value().getCategory(context.registryAccess()));
            } else {
                handler.select(accessory);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
