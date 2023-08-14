package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.json.GsonUtil;

public class SkinTypedTextureReferenceProperty extends PalladiumProperty<SkinTypedValue<TextureReference>> {

    public SkinTypedTextureReferenceProperty(String key) {
        super(key);
    }

    @Override
    public SkinTypedValue<TextureReference> fromJSON(JsonElement jsonElement) {
        return SkinTypedValue.fromJSON(jsonElement, element -> GsonUtil.convertToTextureReference(element, this.getKey()));
    }

    @Override
    public JsonElement toJSON(SkinTypedValue<TextureReference> value) {
        return value.toJson(textureReference -> new JsonPrimitive(textureReference.toString()));
    }

    @Override
    public SkinTypedValue<TextureReference> fromNBT(Tag tag, SkinTypedValue<TextureReference> defaultValue) {
        if (tag instanceof CompoundTag compoundTag) {
            return new SkinTypedValue<>(TextureReference.parse(compoundTag.getString("Normal")), TextureReference.parse(compoundTag.getString("Slim")));
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(SkinTypedValue<TextureReference> value) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Normal", value.getNormal().toString());
        tag.putString("Slim", value.getSlim().toString());
        return tag;
    }

    @Override
    public SkinTypedValue<TextureReference> fromBuffer(FriendlyByteBuf buf) {
        return new SkinTypedValue<>(TextureReference.fromBuffer(buf), TextureReference.fromBuffer(buf));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        SkinTypedValue<TextureReference> val = (SkinTypedValue<TextureReference>) value;
        val.getNormal().toBuffer(buf);
        val.getSlim().toBuffer(buf);
    }
}
