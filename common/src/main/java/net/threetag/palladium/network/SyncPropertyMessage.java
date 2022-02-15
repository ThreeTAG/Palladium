package net.threetag.palladium.network;

import com.mojang.datafixers.util.Pair;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;

public class SyncPropertyMessage extends BaseS2CMessage {

    private final int entityId;
    private final CompoundTag tag;

    public SyncPropertyMessage(int entityId, PalladiumProperty<?> property, Object value) {
        this(entityId, List.of(Pair.of(property, value)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public SyncPropertyMessage(int entityId, List<Pair<PalladiumProperty<?>, Object>> propertyValues) {
        this.entityId = entityId;
        this.tag = new CompoundTag();

        for (Pair<PalladiumProperty<?>, Object> property : propertyValues) {
            if (property.getSecond() == null) {
                this.tag.put(property.getFirst().getKey(), StringTag.valueOf("null"));
            } else {
                PalladiumProperty property1 = property.getFirst();
                this.tag.put(property.getFirst().getKey(), property1.toNBT(property.getSecond()));
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
