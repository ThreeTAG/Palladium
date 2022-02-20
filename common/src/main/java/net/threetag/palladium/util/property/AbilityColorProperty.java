package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.power.ability.AbilityColor;

public class AbilityColorProperty extends PalladiumProperty<AbilityColor> {

    public AbilityColorProperty(String key) {
        super(key);
    }

    @Override
    public AbilityColor fromJSON(JsonElement jsonElement) {
        AbilityColor color = AbilityColor.getByName(jsonElement.getAsString());
        if (color == null) {
            throw new JsonParseException("Unknown ability color '" + jsonElement.getAsString() + "'");
        }
        return color;
    }

    @Override
    public JsonElement toJSON(AbilityColor value) {
        return new JsonPrimitive(value.name());
    }

    @Override
    public AbilityColor fromNBT(Tag tag, AbilityColor defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return AbilityColor.getByName(stringTag.getAsString());
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(AbilityColor value) {
        return StringTag.valueOf(value.name());
    }

    @Override
    public AbilityColor fromBuffer(FriendlyByteBuf buf) {
        return AbilityColor.getByName(buf.readUtf());
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeUtf(((AbilityColor) value).name());
    }
}
