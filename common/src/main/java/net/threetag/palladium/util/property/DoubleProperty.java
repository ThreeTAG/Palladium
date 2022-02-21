package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class DoubleProperty extends PalladiumProperty<Double> {

    public DoubleProperty(String key) {
        super(key);
    }

    @Override
    public Double fromJSON(JsonElement jsonElement) {
        return jsonElement.getAsDouble();
    }

    @Override
    public JsonElement toJSON(Double value) {
        return new JsonPrimitive(value);
    }

    @Override
    public Double fromNBT(Tag tag, Double defaultValue) {
        if (tag instanceof DoubleTag doubleTag) {
            return doubleTag.getAsDouble();
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Double value) {
        return DoubleTag.valueOf(value);
    }

    @Override
    public Double fromBuffer(FriendlyByteBuf buf) {
        return buf.readDouble();
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeDouble((Double) value);
    }
}
