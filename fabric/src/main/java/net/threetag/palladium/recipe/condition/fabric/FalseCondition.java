package net.threetag.palladium.recipe.condition.fabric;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

public final class FalseCondition implements IRecipeCondition {

    public static final FalseCondition INSTANCE = new FalseCondition();
    private static final ResourceLocation NAME = new ResourceLocation("forge", "false");

    private FalseCondition() {
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return false;
    }

    @Override
    public String toString() {
        return "false";
    }

    public static class Serializer implements IRecipeConditionSerializer<FalseCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, FalseCondition value) {
        }

        @Override
        public FalseCondition read(JsonObject json) {
            return FalseCondition.INSTANCE;
        }

        @Override
        public ResourceLocation getID() {
            return FalseCondition.NAME;
        }
    }
}
