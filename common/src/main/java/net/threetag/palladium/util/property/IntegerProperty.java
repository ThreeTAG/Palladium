package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class IntegerProperty extends PalladiumProperty<Integer> {

    public IntegerProperty(String key) {
        super(key);
    }

    @Override
    public Integer fromJSON(JsonElement jsonElement) {
        return jsonElement.getAsInt();
    }

    @Override
    public JsonElement toJSON(Integer value) {
        return new JsonPrimitive(value);
    }

    @Override
    public Integer fromNBT(Tag tag, Integer defaultValue) {
        if (tag instanceof IntTag intTag) {
            return intTag.getAsInt();
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Integer value) {
        return IntTag.valueOf(value);
    }

    @Override
    public Integer fromBuffer(FriendlyByteBuf buf) {
        return buf.readInt();
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeInt((Integer) value);
    }

    @Override
    public String getPropertyType() {
        return "integer";
    }
}
