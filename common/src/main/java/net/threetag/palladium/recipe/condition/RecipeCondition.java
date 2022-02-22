package net.threetag.palladium.recipe.condition;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class RecipeCondition {

    @ExpectPlatform
    public static IRecipeConditionSerializer<?> register(IRecipeConditionSerializer<?> serializer) {
        throw new AssertionError();
    }

}
