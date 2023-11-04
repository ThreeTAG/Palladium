package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;

import java.util.Objects;

public class SyncPropertyMessage extends MessageS2C {

    private final int entityId;
    public final CompoundTag tag;

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

    @Override
    public void handle(MessageContext context) {
        this.handleClient();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Environment(EnvType.CLIENT)
    public void handleClient() {
        Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(this.entityId);
        if (entity != null) {
            EntityPropertyHandler.getHandler(entity).ifPresent(handler -> {
                for (String key : this.tag.getAllKeys()) {
                    PalladiumProperty property = handler.getPropertyByName(key);
                    if (property != null) {
                        handler.setRaw(property, property.fromNBT(this.tag.get(property.getKey()), handler.getDefault(property)));
                    }
                }
            });
        }
    }
}
