package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.chunk.ChunkSource;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public class PalladiumNetwork {

    public static void init() {
        // Server -> Client
        registerS2C(SyncEntityPowersPacket.TYPE, SyncEntityPowersPacket.STREAM_CODEC, SyncEntityPowersPacket::handle);
        registerS2C(SyncAbilityComponentPacket.TYPE, SyncAbilityComponentPacket.STREAM_CODEC, SyncAbilityComponentPacket::handle);
        registerS2C(SyncEnergyBarPacket.TYPE, SyncEnergyBarPacket.STREAM_CODEC, SyncEnergyBarPacket::handle);
        registerS2C(OpenAbilityBuyScreenPacket.TYPE, OpenAbilityBuyScreenPacket.STREAM_CODEC, OpenAbilityBuyScreenPacket::handle);
        registerS2C(SyncAttachmentTypePacket.TYPE, SyncAttachmentTypePacket.STREAM_CODEC, SyncAttachmentTypePacket::handle);

        // Client -> Server
        registerC2S(AbilityKeyChangePacket.TYPE, AbilityKeyChangePacket.STREAM_CODEC, AbilityKeyChangePacket::handle);
        registerC2S(AbilityClickedPacket.TYPE, AbilityClickedPacket.STREAM_CODEC, AbilityClickedPacket::handle);
        registerC2S(BuyAbilityPacket.TYPE, BuyAbilityPacket.STREAM_CODEC, BuyAbilityPacket::handle);

        // Data Sync
        DataSyncUtil.registerEntitySync((entity, consumer) -> {
            if (entity instanceof LivingEntity livingEntity) {
                consumer.accept(SyncEntityPowersPacket.create(livingEntity));
            }
        });
    }

    public static <T extends CustomPacketPayload> void registerS2C(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        if (Platform.getEnvironment() == Env.SERVER) {
            NetworkManager.registerS2CPayloadType(type, codec);
        } else {
            NetworkManager.registerReceiver(NetworkManager.s2c(), type, codec, receiver);
        }
    }

    public static <T extends CustomPacketPayload> void registerC2S(CustomPacketPayload.Type<T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec, NetworkManager.NetworkReceiver<T> receiver) {
        NetworkManager.registerReceiver(NetworkManager.c2s(), type, codec, receiver);
    }

    public static void sendToTrackingAndSelf(Entity entity, CustomPacketPayload packet) {
        if (entity.level().isClientSide()) {
            throw new IllegalStateException("Cannot send clientbound payloads on the client");
        } else {
            if (entity instanceof ServerPlayer player) {
                NetworkManager.sendToPlayer(player, packet);
            }
            for (ServerPlayer player : tracking(entity)) {
                NetworkManager.sendToPlayer(player, packet);
            }
        }
    }

    public static Collection<ServerPlayer> tracking(Entity entity) {
        Objects.requireNonNull(entity, "Entity cannot be null");
        ChunkSource manager = entity.level().getChunkSource();
        if (manager instanceof ServerChunkCache) {
            ChunkMap chunkLoadingManager = ((ServerChunkCache) manager).chunkMap;
            ChunkMap.TrackedEntity tracker = chunkLoadingManager.entityMap.get(entity.getId());
            return (tracker != null ? tracker.seenBy.stream().map(ServerPlayerConnection::getPlayer).collect(Collectors.toUnmodifiableSet()) : Collections.emptySet());
        } else {
            throw new IllegalArgumentException("Only supported on server worlds!");
        }
    }
}
