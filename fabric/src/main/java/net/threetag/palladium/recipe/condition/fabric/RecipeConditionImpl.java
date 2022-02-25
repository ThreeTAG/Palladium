package net.threetag.palladium.recipe.condition.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.recipe.condition.IRecipeCondition;
import net.threetag.palladium.recipe.condition.IRecipeConditionSerializer;

import java.util.HashMap;
import java.util.Map;

public class RecipeConditionImpl {

    private static final Map<ResourceLocation, IRecipeConditionSerializer<?>> CONDITIONS = new HashMap<>();

    static {
        register(TrueCondition.Serializer.INSTANCE);
        register(FalseCondition.Serializer.INSTANCE);
        register(AndCondition.Serializer.INSTANCE);
        register(NotCondition.Serializer.INSTANCE);
        register(OrCondition.Serializer.INSTANCE);
        register(ItemExistsCondition.Serializer.INSTANCE);
        register(TagEmptyCondition.Serializer.INSTANCE);
        register(ModLoadedCondition.Serializer.INSTANCE);
    }

    public static IRecipeConditionSerializer<?> register(IRecipeConditionSerializer<?> serializer) {
        ResourceLocation key = serializer.getID();
        if (CONDITIONS.containsKey(key))
            throw new IllegalStateException("Duplicate recipe condition serializer: " + key);
        CONDITIONS.put(key, serializer);
        return serializer;
    }

    public static boolean processConditions(JsonArray conditions) {
        for (int x = 0; x < conditions.size(); x++) {
            if (!conditions.get(x).isJsonObject())
                throw new JsonSyntaxException("Conditions must be an array of JsonObjects");

            JsonObject json = conditions.get(x).getAsJsonObject();
            if (!getCondition(json).test())
                return false;
        }
        return true;
    }

    public static IRecipeCondition getCondition(JsonObject json) {
        ResourceLocation type = new ResourceLocation(GsonHelper.getAsString(json, "type"));
        IRecipeConditionSerializer<?> serializer = CONDITIONS.get(type);
        if (serializer == null)
            throw new JsonSyntaxException("Unknown condition type: " + type.toString());
        return serializer.read(json);
    }

    public static <T extends IRecipeCondition> JsonObject serialize(T condition) {
        @SuppressWarnings("unchecked")
        IRecipeConditionSerializer<T> serializer = (IRecipeConditionSerializer<T>) CONDITIONS.get(condition.getID());
        if (serializer == null)
            throw new JsonSyntaxException("Unknown condition type: " + condition.getID().toString());
        return serializer.getJson(condition);
    }
}
