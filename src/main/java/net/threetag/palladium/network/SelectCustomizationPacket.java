package net.threetag.palladium.network;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public record SelectCustomizationPacket(Holder<Customization> customization) implements CustomPacketPayload {

    public static final Type<SelectCustomizationPacket> TYPE = new Type<>(Palladium.id("select_customization"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SelectCustomizationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(PalladiumRegistryKeys.CUSTOMIZATION), SelectCustomizationPacket::customization,
            SelectCustomizationPacket::new
    );

    public static void handle(SelectCustomizationPacket packet, IPayloadContext context) {
        var handler = EntityCustomizationHandler.get(context.player());
        if (handler.isSelected(packet.customization)) {
            handler.unselect(packet.customization.value().getCategory(context.player().registryAccess()));
        } else {
            handler.select(packet.customization);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
