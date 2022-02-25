package net.threetag.palladium.recipe.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.util.GsonHelper;

public class RecipeCondition {

    @ExpectPlatform
    public static IRecipeConditionSerializer<?> register(IRecipeConditionSerializer<?> serializer) {
        throw new AssertionError();
    }

    public static boolean processConditions(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            processConditions(GsonHelper.getAsJsonArray(json, memberName));
        }
        return true;
    }

    @ExpectPlatform
    public static boolean processConditions(JsonArray conditions) {
        throw new AssertionError();
    }

}
