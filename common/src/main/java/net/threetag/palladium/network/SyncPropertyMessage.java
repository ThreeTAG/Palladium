package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumPropertyValue;

import java.util.List;

public class SyncPropertyMessage extends BaseS2CMessage {

    private final int entityId;
    private final CompoundTag tag;

    public SyncPropertyMessage(int entityId, PalladiumPropertyValue<?> propertyValue) {
        this(entityId, List.of(propertyValue));
    }

    public SyncPropertyMessage(int entityId, List<PalladiumPropertyValue<?>> propertyValues) {
        this.entityId = entityId;
        this.tag = new CompoundTag();

        for (PalladiumPropertyValue<?> property : propertyValues) {
            if (property.value() == null) {
                this.tag.put(property.getData().getKey(), StringTag.valueOf("null"));
            } else {
                this.tag.put(property.getData().getKey(), property.toNBT());
            }
        }
    }

    public SyncPropertyMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.tag = buf.readNbt();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_PROPERTY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeNbt(this.tag);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(this.entityId);
            if (entity != null) {
                EntityPropertyHandler.getHandler(entity).fromNBT(this.tag);
            }
        });
    }
}
