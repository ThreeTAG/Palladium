package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec2;

public class Vec2Property extends PalladiumProperty<Vec2> {

    public Vec2Property(String key) {
        super(key);
    }

    @Override
    public Vec2 fromJSON(JsonElement jsonElement) {
        JsonArray array = jsonElement.getAsJsonArray();

        if (array.size() != 2) {
            throw new JsonParseException(this.getKey() + " must be an array with two float");
        }

        return new Vec2(array.get(0).getAsFloat(), array.get(1).getAsFloat());
    }

    @Override
    public JsonElement toJSON(Vec2 value) {
        JsonArray array = new JsonArray();
        array.add(value.x);
        array.add(value.y);
        return array;
    }

    @Override
    public Vec2 fromNBT(Tag tag, Vec2 defaultValue) {
        if (tag instanceof CompoundTag compoundTag) {
            return new Vec2(compoundTag.getFloat("X"), compoundTag.getFloat("Y"));
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Vec2 value) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putFloat("X", value.x);
        compoundTag.putFloat("Y", value.y);
        return compoundTag;
    }

    @Override
    public Vec2 fromBuffer(FriendlyByteBuf buf) {
        return new Vec2(buf.readFloat(), buf.readFloat());
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        var vec = (Vec2) value;
        buf.writeFloat(vec.x);
        buf.writeFloat(vec.y);
    }

    @Override
    public String getString(Vec2 value) {
        return value != null ? "[" + value.x + " / " + value.y + "]" : null;
    }
}
