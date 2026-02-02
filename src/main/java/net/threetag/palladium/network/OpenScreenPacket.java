package net.threetag.palladium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;

public record OpenScreenPacket(Identifier screenId) implements CustomPacketPayload {

    public static final Type<OpenScreenPacket> TYPE = new Type<>(Palladium.id("open_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenScreenPacket> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, OpenScreenPacket::screenId,
            OpenScreenPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenScreenPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> Palladium.PROXY.openScreen(packet.screenId));
    }
}
