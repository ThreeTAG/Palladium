package net.threetag.palladium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.unlocking.BuyableUnlockingHandler;
import org.jetbrains.annotations.NotNull;

public record BuyAbilityPacket(AbilityReference reference) implements CustomPacketPayload {

    public static final Type<BuyAbilityPacket> TYPE = new Type<>(Palladium.id("buy_ability"));

    public static final StreamCodec<RegistryFriendlyByteBuf, BuyAbilityPacket> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, BuyAbilityPacket::reference,
            BuyAbilityPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BuyAbilityPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> packet.reference.optional(context.player(), null).ifPresent(ability -> {
            if(ability.getAbility().getStateManager().getUnlockingHandler() instanceof BuyableUnlockingHandler buyable) {
                if(buyable.hasEnoughCurrency(context.player())) {
                    buyable.consumeCurrency(context.player());
                    buyable.unlock(context.player(), ability);
                }
            }
        }));
    }

}
