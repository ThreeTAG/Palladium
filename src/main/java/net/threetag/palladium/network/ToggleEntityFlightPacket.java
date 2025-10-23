package net.threetag.palladium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.entity.flight.EntityFlightHandler;

public record ToggleEntityFlightPacket(boolean start) implements CustomPacketPayload {

    public static final Type<ToggleEntityFlightPacket> TYPE = new Type<>(Palladium.id("toggle_entity_flight"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleEntityFlightPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, ToggleEntityFlightPacket::start,
            ToggleEntityFlightPacket::new
    );

    public static void handle(ToggleEntityFlightPacket packet, IPayloadContext context) {
        var flight = EntityFlightHandler.get(context.player(), PalladiumEntityDataTypes.FLIGHT.get());

        if (packet.start) {
            flight.startFlight();
        } else {
            flight.stopFlight();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
