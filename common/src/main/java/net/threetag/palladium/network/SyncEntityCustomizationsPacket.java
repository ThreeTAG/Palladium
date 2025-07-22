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

import java.util.List;

public record SyncEntityCustomizationsPacket(int entityId,
                                             List<ResourceKey<Customization>> selected) implements CustomPacketPayload {

    public static final Type<SyncEntityCustomizationsPacket> TYPE = new Type<>(Palladium.id("sync_entity_customizations"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncEntityCustomizationsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, SyncEntityCustomizationsPacket::entityId,
            ResourceKey.streamCodec(PalladiumRegistryKeys.CUSTOMIZATION).apply(ByteBufCodecs.list()), SyncEntityCustomizationsPacket::selected,
            SyncEntityCustomizationsPacket::new
    );

    @Override
    public @NotNull Type<SyncEntityCustomizationsPacket> type() {
        return TYPE;
    }

    public static void handle(SyncEntityCustomizationsPacket packet, NetworkManager.PacketContext context) {
        if (context.getEnvironment() == Env.CLIENT) {
            context.queue(() -> handleClient(packet, context));
        }
    }

    @Environment(EnvType.CLIENT)
    public static void handleClient(SyncEntityCustomizationsPacket packet, NetworkManager.PacketContext context) {
        Level level = Minecraft.getInstance().level;
        if (level != null && level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
            var handler = EntityCustomizationHandler.get(livingEntity);
            for (ResourceKey<Customization> key : packet.selected) {
                context.registryAccess().get(key).ifPresent(handler::select);
            }
        }
    }

    public static SyncEntityCustomizationsPacket create(LivingEntity livingEntity) {
        var handler = EntityCustomizationHandler.get(livingEntity);
        return new SyncEntityCustomizationsPacket(livingEntity.getId(), handler.getSelected().stream().map(accessoryHolder -> accessoryHolder.unwrapKey().orElseThrow()).toList());
    }
}
