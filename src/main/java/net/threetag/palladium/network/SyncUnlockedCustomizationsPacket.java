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

import java.util.List;

public record SyncUnlockedCustomizationsPacket(
        List<Holder<Customization>> unlocked) implements CustomPacketPayload {

    public static final Type<SyncUnlockedCustomizationsPacket> TYPE = new Type<>(Palladium.id("sync_unlocked_customizations"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncUnlockedCustomizationsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(PalladiumRegistryKeys.CUSTOMIZATION).apply(ByteBufCodecs.list()), SyncUnlockedCustomizationsPacket::unlocked,
            SyncUnlockedCustomizationsPacket::new
    );

    public static void handle(SyncUnlockedCustomizationsPacket packet, IPayloadContext context) {
        var handler = EntityCustomizationHandler.get(context.player());
        handler.setUnlocked(packet.unlocked);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
