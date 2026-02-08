package net.threetag.palladium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Optional;

public record OpenCustomizationScreenPacket(
        Optional<ResourceKey<CustomizationCategory>> category) implements CustomPacketPayload {

    public static final Type<OpenCustomizationScreenPacket> TYPE = new Type<>(Palladium.id("open_customization_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenCustomizationScreenPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).apply(ByteBufCodecs::optional), OpenCustomizationScreenPacket::category,
            OpenCustomizationScreenPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenCustomizationScreenPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> Palladium.PROXY.openCustomizationScreen(packet.category.orElse(null)));
    }
}
