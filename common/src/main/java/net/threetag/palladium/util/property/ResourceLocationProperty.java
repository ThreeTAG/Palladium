package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationProperty extends PalladiumProperty<ResourceLocation> {

    public ResourceLocationProperty(String key) {
        super(key);
    }

    @Override
    public ResourceLocation fromJSON(JsonElement jsonElement) {
        return new ResourceLocation(jsonElement.getAsString());
    }

    @Override
    public JsonElement toJSON(ResourceLocation value) {
        return new JsonPrimitive(value.toString());
    }

    @Override
    public ResourceLocation fromNBT(Tag tag, ResourceLocation defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return new ResourceLocation(stringTag.getAsString());
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(ResourceLocation value) {
        return StringTag.valueOf(value.toString());
    }

    @Override
    public ResourceLocation fromBuffer(FriendlyByteBuf buf) {
        return buf.readResourceLocation();
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeResourceLocation((ResourceLocation) value);
    }

    @Override
    public String getPropertyType() {
        return "resource_location";
    }
}
