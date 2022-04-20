package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Arrays;

public abstract class EnumPalladiumProperty<T> extends PalladiumProperty<T> {

    public EnumPalladiumProperty(String key) {
        super(key);
    }

    public abstract T[] getValues();

    public abstract String getNameFromEnum(T value);

    public T getByName(String name) {
        for (T value : this.getValues()) {
            if (getNameFromEnum(value).equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String getDescription() {
        String desc = super.getDescription();
        if (!desc.endsWith(".")) {
            desc += ".";
        }
        return desc + " Possible values: " + Arrays.toString(Arrays.stream(this.getValues()).map(this::getNameFromEnum).toList().toArray());
    }

    @Override
    public T fromJSON(JsonElement jsonElement) {
        T value = getByName(jsonElement.getAsString());
        if (value == null) {
            throw new JsonParseException("Unknown " + this.getKey() + " '" + jsonElement.getAsString() + "'");
        }
        return value;
    }

    @Override
    public JsonElement toJSON(T value) {
        return new JsonPrimitive(getNameFromEnum(value));
    }

    @Override
    public T fromNBT(Tag tag, T defaultValue) {
        if (tag instanceof StringTag stringTag) {
            T value = this.getByName(stringTag.getAsString());

            if (value != null) {
                return value;
            }
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(T value) {
        return StringTag.valueOf(this.getNameFromEnum(value));
    }

    @Override
    public T fromBuffer(FriendlyByteBuf buf) {
        return this.getByName(buf.readUtf());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeUtf(this.getNameFromEnum((T) value));
    }
}
