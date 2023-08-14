package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.client.dynamictexture.TextureReference;

public class TextureReferenceProperty extends PalladiumProperty<TextureReference> {

    public TextureReferenceProperty(String key) {
        super(key);
    }

    @Override
    public TextureReference fromJSON(JsonElement jsonElement) {
        return TextureReference.parse(jsonElement.getAsString());
    }

    @Override
    public JsonElement toJSON(TextureReference value) {
        return new JsonPrimitive(value.toString());
    }

    @Override
    public TextureReference fromNBT(Tag tag, TextureReference defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return TextureReference.parse(stringTag.getAsString());
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(TextureReference value) {
        return StringTag.valueOf(value.toString());
    }

    @Override
    public TextureReference fromBuffer(FriendlyByteBuf buf) {
        return TextureReference.fromBuffer(buf);
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        ((TextureReference) value).toBuffer(buf);
    }

    @Override
    public String getPropertyType() {
        return "texture_reference";
    }
}
