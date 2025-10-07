package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.SwingingFlightType;

public record SyncSwingAnchorPacket(int entityId, BlockPos blockPos) implements CustomPacketPayload {

    public static final Type<SyncSwingAnchorPacket> TYPE = new Type<>(Palladium.id("sync_swinging_anchor"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSwingAnchorPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncSwingAnchorPacket::entityId,
            BlockPos.STREAM_CODEC, SyncSwingAnchorPacket::blockPos,
            SyncSwingAnchorPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncSwingAnchorPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> {
            var entity = context.getPlayer().level().getEntity(packet.entityId);

            if (entity instanceof Player player) {
                var flightHandler = EntityFlightHandler.get(player);

                if (flightHandler.getFlightType() instanceof SwingingFlightType swinging &&
                        flightHandler.getController() instanceof SwingingFlightType.Controller controller) {
                    controller.setAnchor(player, packet.blockPos, swinging);
                }
            }
        });
    }
}