package net.threetag.palladium.recipe.condition.fabric;

import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

public record ModLoadedCondition(String modid) implements IRecipeCondition {

    private static final ResourceLocation NAME = new ResourceLocation("forge", "mod_loaded");

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return Platform.getOptionalMod(this.modid).isPresent();
    }

    @Override
    public String toString() {
        return "mod_loaded(\"" + modid + "\")";
    }

    public static class Serializer implements IRecipeConditionSerializer<ModLoadedCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, ModLoadedCondition value) {
            json.addProperty("modid", value.modid);
        }

        @Override
        public ModLoadedCondition read(JsonObject json) {
            return new ModLoadedCondition(GsonHelper.getAsString(json, "modid"));
        }

        @Override
        public ResourceLocation getID() {
            return ModLoadedCondition.NAME;
        }
    }
}

