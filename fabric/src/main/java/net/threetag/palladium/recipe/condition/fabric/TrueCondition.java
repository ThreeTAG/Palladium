package net.threetag.palladium.recipe.condition.fabric;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

public final class TrueCondition implements IRecipeCondition {

    public static final TrueCondition INSTANCE = new TrueCondition();
    private static final ResourceLocation NAME = new ResourceLocation("forge", "true");

    private TrueCondition() {
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return true;
    }

    @Override
    public String toString() {
        return "true";
    }

    public static class Serializer implements IRecipeConditionSerializer<TrueCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, TrueCondition value) {
        }

        @Override
        public TrueCondition read(JsonObject json) {
            return TrueCondition.INSTANCE;
        }

        @Override
        public ResourceLocation getID() {
            return TrueCondition.NAME;
        }
    }
}

