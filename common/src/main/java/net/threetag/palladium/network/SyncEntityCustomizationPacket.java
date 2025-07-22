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
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import org.jetbrains.annotations.NotNull;

public record SyncEntityCustomizationPacket(int entityId, ResourceKey<Customization> customization) implements CustomPacketPayload {

    public static final Type<SyncEntityCustomizationPacket> TYPE = new Type<>(Palladium.id("sync_entity_customization"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityCustomizationPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncEntityCustomizationPacket::entityId,
            ResourceKey.streamCodec(PalladiumRegistryKeys.CUSTOMIZATION), SyncEntityCustomizationPacket::customization,
            SyncEntityCustomizationPacket::new
    );

    @Override
    public @NotNull Type<SyncEntityCustomizationPacket> type() {
        return TYPE;
    }

    public static void handle(SyncEntityCustomizationPacket packet, NetworkManager.PacketContext context) {
        if (context.getEnvironment() == Env.CLIENT) {
            context.queue(() -> handleClient(packet, context));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncEntityCustomizationPacket packet, NetworkManager.PacketContext context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
            var handler = EntityCustomizationHandler.get(livingEntity);
            context.registryAccess().get(packet.customization).ifPresent(handler::select);
        }
    }
}
