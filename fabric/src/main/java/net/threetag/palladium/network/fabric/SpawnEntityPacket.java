package net.threetag.palladium.network.fabric;

import dev.architectury.extensions.network.EntitySpawnExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.network.ExtendedEntitySpawnData;

import java.util.function.Consumer;

public class SpawnEntityPacket {

    private static final ResourceLocation PACKET_ID = Palladium.id("spawn_entity_packet");

    public static Packet<?> create(Entity entity) {
        if (entity.level.isClientSide()) {
            throw new IllegalStateException("SpawnPacketUtil.create called on the logical client!");
        }
        var buffer = PacketByteBufs.create();
        buffer.writeVarInt(Registry.ENTITY_TYPE.getId(entity.getType()));
        buffer.writeUUID(entity.getUUID());
        buffer.writeVarInt(entity.getId());
        var position = entity.position();
        buffer.writeDouble(position.x);
        buffer.writeDouble(position.y);
        buffer.writeDouble(position.z);
        buffer.writeFloat(entity.getXRot());
        buffer.writeFloat(entity.getYRot());
        buffer.writeFloat(entity.getYHeadRot());
        var deltaMovement = entity.getDeltaMovement();
        buffer.writeDouble(deltaMovement.x);
        buffer.writeDouble(deltaMovement.y);
        buffer.writeDouble(deltaMovement.z);
        if (entity instanceof ExtendedEntitySpawnData ext) {
            ext.saveAdditionalSpawnData(buffer);
        }
        return ServerPlayNetworking.createS2CPacket(PACKET_ID, buffer);
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        public static void register() {
            ClientPlayNetworking.registerGlobalReceiver(PACKET_ID, (client, handler, buf, responseSender) -> {
                receive(buf, client::execute);
            });
        }

        public static void receive(FriendlyByteBuf buf, Consumer<Runnable> consumer) {
            var entityTypeId = buf.readVarInt();
            var uuid = buf.readUUID();
            var id = buf.readVarInt();
            var x = buf.readDouble();
            var y = buf.readDouble();
            var z = buf.readDouble();
            var xRot = buf.readFloat();
            var yRot = buf.readFloat();
            var yHeadRot = buf.readFloat();
            var deltaX = buf.readDouble();
            var deltaY = buf.readDouble();
            var deltaZ = buf.readDouble();
            // Retain this buffer so we can use it in the queued task (EntitySpawnExtension)
            buf.retain();
            consumer.accept(() -> {
                var entityType = Registry.ENTITY_TYPE.byId(entityTypeId);
                if (entityType == null) {
                    throw new IllegalStateException("Entity type (" + entityTypeId + ") is unknown, spawning at (" + x + ", " + y + ", " + z + ")");
                }
                if (Minecraft.getInstance().level == null) {
                    throw new IllegalStateException("Client world is null!");
                }
                var entity = entityType.create(Minecraft.getInstance().level);
                if (entity == null) {
                    throw new IllegalStateException("Created entity is null!");
                }
                entity.setUUID(uuid);
                entity.setId(id);
                entity.setPacketCoordinates(x, y, z);
                entity.absMoveTo(x, y, z, yRot, xRot);
                entity.setYHeadRot(yHeadRot);
                entity.setYBodyRot(yHeadRot);
                if (entity instanceof EntitySpawnExtension ext) {
                    ext.loadAdditionalSpawnData(buf);
                }
                buf.release();
                Minecraft.getInstance().level.putNonPlayerEntity(id, entity);
                entity.lerpMotion(deltaX, deltaY, deltaZ);
            });
        }
    }

}
