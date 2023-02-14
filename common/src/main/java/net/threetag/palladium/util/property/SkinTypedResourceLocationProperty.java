package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.json.GsonUtil;

public class SkinTypedResourceLocationProperty extends PalladiumProperty<SkinTypedValue<ResourceLocation>> {

    public SkinTypedResourceLocationProperty(String key) {
        super(key);
    }

    @Override
    public SkinTypedValue<ResourceLocation> fromJSON(JsonElement jsonElement) {
        return SkinTypedValue.fromJSON(jsonElement, element -> GsonUtil.convertToResourceLocation(element, this.getKey()));
    }

    @Override
    public JsonElement toJSON(SkinTypedValue<ResourceLocation> value) {
        return value.toJson(resourceLocation -> new JsonPrimitive(resourceLocation.toString()));
    }

    @Override
    public SkinTypedValue<ResourceLocation> fromNBT(Tag tag, SkinTypedValue<ResourceLocation> defaultValue) {
        if (tag instanceof CompoundTag compoundTag) {
            return new SkinTypedValue<>(new ResourceLocation(compoundTag.getString("Normal"), compoundTag.getString("Slim")));
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(SkinTypedValue<ResourceLocation> value) {
        CompoundTag tag = new CompoundTag();
        tag.putString("Normal", value.getNormal().toString());
        tag.putString("Slim", value.getSlim().toString());
        return tag;
    }

    @Override
    public SkinTypedValue<ResourceLocation> fromBuffer(FriendlyByteBuf buf) {
        return new SkinTypedValue<>(buf.readResourceLocation(), buf.readResourceLocation());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        SkinTypedValue<ResourceLocation> val = (SkinTypedValue<ResourceLocation>) value;
        buf.writeResourceLocation(val.getNormal());
        buf.writeResourceLocation(val.getSlim());
    }
}
