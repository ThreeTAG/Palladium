package net.threetag.palladium.recipe.condition.fabric;

import com.google.gson.JsonObject;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

import java.util.Iterator;

public class TagEmptyCondition implements IRecipeCondition {

    private static final ResourceLocation NAME = new ResourceLocation("forge", "tag_empty");
    private final TagKey<Item> tag;

    public TagEmptyCondition(String location) {
        this(new ResourceLocation(location));
    }

    public TagEmptyCondition(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    public TagEmptyCondition(ResourceLocation tag) {
        this.tag = TagKey.create(Registry.ITEM_REGISTRY, tag);
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return !Registry.ITEM.getTag(tag).map(HolderSet.Named::iterator).map(Iterator::hasNext).orElse(false);
    }

    @Override
    public String toString() {
        return "tag_empty(\"" + tag.location() + "\")";
    }

    public static class Serializer implements IRecipeConditionSerializer<TagEmptyCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, TagEmptyCondition value) {
            json.addProperty("tag", value.tag.location().toString());
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

