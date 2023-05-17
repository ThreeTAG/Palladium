package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

import java.awt.*;

public class ColorProperty extends PalladiumProperty<Color> {

    public ColorProperty(String key) {
        super(key);
    }

    @Override
    public Color fromJSON(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return Color.decode(jsonElement.getAsString());
        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            if (array.size() == 3) {
                return new Color(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt());
            } else if (array.size() == 4) {
                return new Color(array.get(0).getAsInt(), array.get(1).getAsInt(), array.get(2).getAsInt(), array.get(3).getAsInt());
            } else {
                throw new JsonParseException("Color array must either have 3 (RGB) or 4 (RGBA) integers");
            }
        } else {
            throw new JsonParseException("Color must either be defined as RGB-string or array of integers");
        }
    }

    @Override
    public JsonElement toJSON(Color value) {
        if (value.getAlpha() != 255) {
            JsonArray array = new JsonArray();
            array.add(value.getRed());
            array.add(value.getGreen());
            array.add(value.getBlue());
            array.add(value.getAlpha());
            return array;
        } else {
            return new JsonPrimitive(String.format("#%02X%02X%02X", value.getRed(), value.getGreen(), value.getBlue()));
        }
    }

    @Override
    public Color fromNBT(Tag tag, Color defaultValue) {
        if (tag instanceof CompoundTag compoundTag) {
            return new Color(compoundTag.getInt("Red"), compoundTag.getInt("Green"), compoundTag.getInt("Blue"), compoundTag.getInt("Alpha"));
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Color value) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Red", value.getRed());
        tag.putInt("Green", value.getGreen());
        tag.putInt("Blue", value.getBlue());
        tag.putInt("Alpha", value.getAlpha());
        return tag;
    }

    @Override
    public Color fromBuffer(FriendlyByteBuf buf) {
        return new Color(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        Color color = (Color) value;
        buf.writeInt(color.getRed());
        buf.writeInt(color.getGreen());
        buf.writeInt(color.getBlue());
        buf.writeInt(color.getAlpha());
    }

    @Override
    public String getPropertyType() {
        return "color";
    }
}
