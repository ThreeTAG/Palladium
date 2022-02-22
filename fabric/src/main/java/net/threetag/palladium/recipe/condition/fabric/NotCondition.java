package net.threetag.palladium.recipe.condition.fabric;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

public record NotCondition(IRecipeCondition child) implements IRecipeCondition {

    private static final ResourceLocation NAME = new ResourceLocation("forge", "not");

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return !child.test();
    }

    @Override
    public String toString() {
        return "!" + child;
    }

    public static class Serializer implements IRecipeConditionSerializer<NotCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, NotCondition value) {
            json.add("value", RecipeConditionImpl.serialize(value.child));
        }

        @Override
        public NotCondition read(JsonObject json) {
            return new NotCondition(RecipeConditionImpl.getCondition(GsonHelper.getAsJsonObject(json, "value")));
        }

        @Override
        public ResourceLocation getID() {
            return NotCondition.NAME;
        }
    }
}
