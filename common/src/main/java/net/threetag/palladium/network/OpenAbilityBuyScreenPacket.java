package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilityReference;

public record OpenAbilityBuyScreenPacket(AbilityReference reference, boolean available) implements CustomPacketPayload {

    public static final Type<OpenAbilityBuyScreenPacket> TYPE = new Type<>(Palladium.id("open_ability_buy_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenAbilityBuyScreenPacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, OpenAbilityBuyScreenPacket::reference,
            ByteBufCodecs.BOOL, OpenAbilityBuyScreenPacket::available,
            OpenAbilityBuyScreenPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenAbilityBuyScreenPacket packet, NetworkManager.PacketContext context) {
        context.queue(() -> Palladium.PROXY.packetHandleOpenAbilityBuyScreen(packet, context));
    }
}
