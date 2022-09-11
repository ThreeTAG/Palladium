package net.threetag.palladium.network.messages;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;

public class SyncPropertyMessage extends MessageS2C {

    private final int entityId;
    private final CompoundTag tag;

    @SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
    public SyncPropertyMessage(int entityId, PalladiumProperty<?> property, Object value) {
        this.entityId = entityId;
        this.tag = new CompoundTag();

        if (value == null) {
            this.tag.put(property.getKey(), StringTag.valueOf("null"));
        } else {
            PalladiumProperty property1 = property;
            this.tag.put(property.getKey(), property1.toNBT(value));
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
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeNbt(this.tag);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void handle(MessageContext context) {
        Entity entity = Minecraft.getInstance().level.getEntity(this.entityId);
        if (entity != null) {
            EntityPropertyHandler handler = EntityPropertyHandler.getHandler(entity);
            for (String key : this.tag.getAllKeys()) {
                PalladiumProperty property = handler.getPropertyByName(key);
                if (property != null) {
                    handler.setRaw(property, property.fromNBT(this.tag.get(property.getKey()), handler.getDefault(property)));
                }
            }
        }
    }
}
