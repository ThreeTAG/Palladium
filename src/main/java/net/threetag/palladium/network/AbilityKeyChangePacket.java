package net.threetag.palladium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;
import org.jetbrains.annotations.NotNull;

public record AbilityKeyChangePacket(AbilityReference reference, boolean pressed) implements CustomPacketPayload {

    public static final Type<AbilityKeyChangePacket> TYPE = new Type<>(Palladium.id("ability_key_change"));

    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityKeyChangePacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityKeyChangePacket::reference,
            ByteBufCodecs.BOOL, AbilityKeyChangePacket::pressed,
            AbilityKeyChangePacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(AbilityKeyChangePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> packet.reference.optional(context.player(), null).ifPresent(ability -> {
            if(ability.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler handler) {
                if(packet.pressed) {
                    handler.onKeyPressed(context.player(), ability);
                } else {
                    handler.onKeyReleased(context.player(), ability);
                }
            }
        }));
    }
}
