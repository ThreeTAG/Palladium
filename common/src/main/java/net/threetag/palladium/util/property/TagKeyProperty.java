package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class TagKeyProperty<T> extends PalladiumProperty<TagKey<T>> {

    private final ResourceKey<Registry<T>> registry;

    public TagKeyProperty(String key, ResourceKey<Registry<T>> registry) {
        super(key);
        this.registry = registry;
    }

    @Override
    public TagKey<T> fromJSON(JsonElement jsonElement) {
        return TagKey.create(this.registry, new ResourceLocation(jsonElement.getAsString()));
    }

    @Override
    public JsonElement toJSON(TagKey<T> value) {
        return new JsonPrimitive(value.location().toString());
    }

    @Override
    public TagKey<T> fromNBT(Tag tag, TagKey<T> defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return TagKey.create(this.registry, new ResourceLocation(stringTag.getAsString()));
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(TagKey<T> value) {
        return StringTag.valueOf(value.location().toString());
    }

    @Override
    public TagKey<T> fromBuffer(FriendlyByteBuf buf) {
        return TagKey.create(this.registry, buf.readResourceLocation());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeResourceLocation(((TagKey<T>) value).location());
    }

    @Override
    public String getString(TagKey<T> value) {
        return value == null ? null : value.location().toString();
    }

    @Override
    public String getPropertyType() {
        return this.registry.location().getPath() + "_tag";
    }
}
