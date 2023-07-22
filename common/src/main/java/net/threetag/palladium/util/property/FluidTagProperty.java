package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class FluidTagProperty extends PalladiumProperty<TagKey<Fluid>> {

    public FluidTagProperty(String key) {
        super(key);
    }

    @Override
    public TagKey<Fluid> fromJSON(JsonElement jsonElement) {
        return TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(jsonElement.getAsString()));
    }

    @Override
    public JsonElement toJSON(TagKey<Fluid> value) {
        return new JsonPrimitive(value.location().toString());
    }

    @Override
    public TagKey<Fluid> fromNBT(Tag tag, TagKey<Fluid> defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(stringTag.getAsString()));
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(TagKey<Fluid> value) {
        return StringTag.valueOf(value.location().toString());
    }

    @Override
    public TagKey<Fluid> fromBuffer(FriendlyByteBuf buf) {
        return TagKey.create(Registry.FLUID_REGISTRY, buf.readResourceLocation());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeResourceLocation(((TagKey<Fluid>) value).location());
    }

    @Override
    public String getString(TagKey<Fluid> value) {
        return value == null ? null : value.location().toString();
    }

    @Override
    public String getPropertyType() {
        return "fluid_tag";
    }
}
