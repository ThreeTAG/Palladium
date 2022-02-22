package net.threetag.palladium.recipe.condition.fabric;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.SerializationTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

public class TagEmptyCondition implements IRecipeCondition {

    private static final ResourceLocation NAME = new ResourceLocation("forge", "tag_empty");
    private final ResourceLocation tag_name;

    public TagEmptyCondition(String location) {
        this(new ResourceLocation(location));
    }

    public TagEmptyCondition(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    public TagEmptyCondition(ResourceLocation tag) {
        this.tag_name = tag;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        Tag<Item> tag = SerializationTags.getInstance().getOrEmpty(Registry.ITEM_REGISTRY).getTag(tag_name);
        return tag == null || tag.getValues().isEmpty();
    }

    @Override
    public String toString() {
        return "tag_empty(\"" + tag_name + "\")";
    }

    public static class Serializer implements IRecipeConditionSerializer<TagEmptyCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, TagEmptyCondition value) {
            json.addProperty("tag", value.tag_name.toString());
        }

        @Override
        public TagEmptyCondition read(JsonObject json) {
            return new TagEmptyCondition(new ResourceLocation(GsonHelper.getAsString(json, "tag")));
        }

        @Override
        public ResourceLocation getID() {
            return TagEmptyCondition.NAME;
        }
    }
}

