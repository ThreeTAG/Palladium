package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class UUIDProperty extends PalladiumProperty<UUID> {

    public UUIDProperty(String key) {
        super(key);
    }

    @Override
    public UUID fromJSON(JsonElement jsonElement) {
        return UUID.fromString(jsonElement.getAsString());
    }

    @Override
    public JsonElement toJSON(UUID value) {
        return new JsonPrimitive(value.toString());
    }

    @Override
    public UUID fromNBT(Tag tag, UUID defaultValue) {
        return NbtUtils.loadUUID(tag);
    }

    @Override
    public Tag toNBT(UUID value) {
        return NbtUtils.createUUID(value);
    }

    @Override
    public UUID fromBuffer(FriendlyByteBuf buf) {
        return buf.readUUID();
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeUUID((UUID) value);
    }
}
