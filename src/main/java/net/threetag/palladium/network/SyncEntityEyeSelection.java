package net.threetag.palladium.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import org.jetbrains.annotations.NotNull;

public record SyncEntityEyeSelection(int entityId, long eyeSelection) implements CustomPacketPayload {

    public static final Type<SyncEntityEyeSelection> TYPE = new Type<>(Palladium.id("sync_entity_eye_selection"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityEyeSelection> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncEntityEyeSelection::entityId,
            ByteBufCodecs.LONG, SyncEntityEyeSelection::eyeSelection,
            SyncEntityEyeSelection::new
    );

    @Override
    public @NotNull Type<SyncEntityEyeSelection> type() {
        return TYPE;
    }

    public static void handle(SyncEntityEyeSelection packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            var entity = context.player().level().getEntity(packet.entityId());

            if (entity instanceof LivingEntity livingEntity) {
                EntityCustomizationHandler.get(livingEntity).setEyeSelection(packet.eyeSelection());
            }
        });
    }

    public static SyncEntityEyeSelection create(LivingEntity livingEntity) {
        var handler = EntityCustomizationHandler.get(livingEntity);
        return new SyncEntityEyeSelection(livingEntity.getId(), handler.getEyeSelection());
    }

}
