package net.threetag.palladium.recipe.condition.fabric;

import com.google.common.base.Joiner;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

import java.util.ArrayList;
import java.util.List;

public class AndCondition implements IRecipeCondition {
    private static final ResourceLocation NAME = new ResourceLocation("forge", "and");
    private final IRecipeCondition[] children;

    public AndCondition(IRecipeCondition... values) {
        if (values == null || values.length == 0)
            throw new IllegalArgumentException("Values must not be empty");

        for (IRecipeCondition child : values) {
            if (child == null)
                throw new IllegalArgumentException("Value must not be null");
        }

        this.children = values;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        for (IRecipeCondition child : children) {
            if (!child.test())
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Joiner.on(" && ").join(children);
    }

    public static class Serializer implements IRecipeConditionSerializer<AndCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, AndCondition value) {
            JsonArray values = new JsonArray();
            for (IRecipeCondition child : value.children)
                values.add(RecipeConditionImpl.serialize(child));
            json.add("values", values);
        }

        @Override
        public AndCondition read(JsonObject json) {
            List<IRecipeCondition> children = new ArrayList<>();
            for (JsonElement j : GsonHelper.getAsJsonArray(json, "values")) {
                if (!j.isJsonObject())
                    throw new JsonSyntaxException("And condition values must be an array of JsonObjects");
                children.add(RecipeConditionImpl.getCondition(j.getAsJsonObject()));
            }
            return new AndCondition(children.toArray(new IRecipeCondition[children.size()]));
        }

        @Override
        public ResourceLocation getID() {
            return AndCondition.NAME;
        }
    }
}
