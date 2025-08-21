package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.NotNull;

public record SyncEntityUnselectCustomizationPacket(int entityId, ResourceKey<CustomizationCategory> categoryKey) implements CustomPacketPayload {

    public static final Type<SyncEntityUnselectCustomizationPacket> TYPE = new Type<>(Palladium.id("sync_entity_unselect_customization"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityUnselectCustomizationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncEntityUnselectCustomizationPacket::entityId,
            ResourceKey.streamCodec(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY), SyncEntityUnselectCustomizationPacket::categoryKey,
            SyncEntityUnselectCustomizationPacket::new
    );

    @Override
    public @NotNull Type<SyncEntityUnselectCustomizationPacket> type() {
        return TYPE;
    }

    public static void handle(SyncEntityUnselectCustomizationPacket packet, NetworkManager.PacketContext context) {
        if (context.getEnvironment() == Env.CLIENT) {
            context.queue(() -> handleClient(packet, context));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncEntityUnselectCustomizationPacket packet, NetworkManager.PacketContext context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
            var handler = EntityCustomizationHandler.get(livingEntity);
            context.registryAccess().get(packet.categoryKey).ifPresent(handler::unselect);
        }
    }
}
