package net.threetag.palladium.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.threetag.palladium.Palladium;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class DataSyncUtil {

    private static final List<EntitySync> ENTITY_SYNC = new ArrayList<>();

    public static void registerEntitySync(EntitySync entitySync) {
        ENTITY_SYNC.add(entitySync);
    }

    @SubscribeEvent
    static void playerJoin(PlayerEvent.PlayerLoggedInEvent e) {
        if (e.getEntity() instanceof ServerPlayer serverPlayer) {
            for (EntitySync entitySync : ENTITY_SYNC) {
                entitySync.gatherPayloads(serverPlayer, payload -> PacketDistributor.sendToPlayer(serverPlayer, payload));
            }
        }
    }

    @SubscribeEvent
    static void startTracking(PlayerEvent.StartTracking e) {
        if (e.getEntity() instanceof ServerPlayer serverPlayer) {
            for (EntitySync entitySync : ENTITY_SYNC) {
                entitySync.gatherPayloads(e.getTarget(), payload -> PacketDistributor.sendToPlayer(serverPlayer, payload));
            }
        }
    }

    @SubscribeEvent
    static void playerRespawn(PlayerEvent.PlayerRespawnEvent e) {
        if (e.getEntity() instanceof ServerPlayer serverPlayer) {
            for (EntitySync entitySync : ENTITY_SYNC) {
                entitySync.gatherPayloads(serverPlayer, payload -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(serverPlayer, payload));
            }
        }
    }

    @SubscribeEvent
    static void playerRespawn(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (e.getEntity() instanceof ServerPlayer serverPlayer) {
            for (EntitySync entitySync : ENTITY_SYNC) {
                entitySync.gatherPayloads(serverPlayer, payload -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(serverPlayer, payload));
            }
        }
    }
    
    @FunctionalInterface
    public interface EntitySync {

        void gatherPayloads(Entity entity, Consumer<CustomPacketPayload> consumer);

    }

}
