package net.threetag.palladium.recipe.condition.fabric;

import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

public class ItemExistsCondition implements IRecipeCondition {

    private static final ResourceLocation NAME = new ResourceLocation("forge", "item_exists");
    private final ResourceLocation item;

    public ItemExistsCondition(String location) {
        this(new ResourceLocation(location));
    }

    public ItemExistsCondition(String namespace, String path) {
        this(new ResourceLocation(namespace, path));
    }

    public ItemExistsCondition(ResourceLocation item) {
        this.item = item;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return Registry.ITEM.containsKey(item);
    }

    @Override
    public String toString() {
        return "item_exists(\"" + item + "\")";
    }

    public static class Serializer implements IRecipeConditionSerializer<ItemExistsCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, ItemExistsCondition value) {
            json.addProperty("item", value.item.toString());
        }

        @Override
        public ItemExistsCondition read(JsonObject json) {
            return new ItemExistsCondition(new ResourceLocation(GsonHelper.getAsString(json, "item")));
        }

        @Override
        public ResourceLocation getID() {
            return ItemExistsCondition.NAME;
        }
    }
}

