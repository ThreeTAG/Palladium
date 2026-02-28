package net.threetag.palladium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import org.jetbrains.annotations.NotNull;

public record SetPlayerEyeSelection(long eyeSelection) implements CustomPacketPayload {

    public static final Type<SetPlayerEyeSelection> TYPE = new Type<>(Palladium.id("set_player_eye_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetPlayerEyeSelection> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.LONG, SetPlayerEyeSelection::eyeSelection,
            SetPlayerEyeSelection::new
    );

    @Override
    public @NotNull Type<SetPlayerEyeSelection> type() {
        return TYPE;
    }

    public static void handle(SetPlayerEyeSelection packet, IPayloadContext context) {
        context.enqueueWork(() -> EntityCustomizationHandler.get(context.player()).setEyeSelection(packet.eyeSelection()));
    }

}
