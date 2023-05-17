package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class Vec3Property extends PalladiumProperty<Vec3> {

    public Vec3Property(String key) {
        super(key);
    }

    @Override
    public Vec3 fromJSON(JsonElement jsonElement) {
        JsonArray array = jsonElement.getAsJsonArray();

        if (array.size() != 3) {
            throw new JsonParseException(this.getKey() + " must be an array with three float");
        }

        return new Vec3(array.get(0).getAsDouble(), array.get(1).getAsDouble(), array.get(2).getAsDouble());
    }

    @Override
    public JsonElement toJSON(Vec3 value) {
        JsonArray array = new JsonArray();
        array.add(value.x);
        array.add(value.y);
        array.add(value.z);
        return array;
    }

    @Override
    public Vec3 fromNBT(Tag tag, Vec3 defaultValue) {
        if (tag instanceof CompoundTag compoundTag) {
            return new Vec3(compoundTag.getDouble("X"), compoundTag.getDouble("Y"), compoundTag.getDouble("Z"));
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Vec3 value) {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putDouble("X", value.x);
        compoundTag.putDouble("Y", value.y);
        compoundTag.putDouble("Z", value.z);
        return compoundTag;
    }

    @Override
    public Vec3 fromBuffer(FriendlyByteBuf buf) {
        return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        var vec = (Vec3) value;
        buf.writeDouble(vec.x);
        buf.writeDouble(vec.y);
        buf.writeDouble(vec.z);
    }

    @Override
    public String getString(Vec3 value) {
        return value != null ? "[" + value.x + " / " + value.y+ " / " + value.z + "]" : null;
    }
}
