package net.threetag.palladium.network;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public record UnselectCustomizationPacket(
        Holder<CustomizationCategory> customizationCategory) implements CustomPacketPayload {

    public static final Type<UnselectCustomizationPacket> TYPE = new Type<>(Palladium.id("unselect_customization"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UnselectCustomizationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY), UnselectCustomizationPacket::customizationCategory,
            UnselectCustomizationPacket::new
    );

    public static void handle(UnselectCustomizationPacket packet, IPayloadContext context) {
        var handler = EntityCustomizationHandler.get(context.player());
        handler.unselect(packet.customizationCategory);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
