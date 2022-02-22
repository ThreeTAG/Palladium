package net.threetag.palladium.fabric.mixin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.threetag.palladium.recipe.condition.fabric.RecipeConditionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(at = @At("HEAD"), method = "fromJson")
    private static void fromJson(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<Recipe<?>> callback) {
        if (!RecipeConditionImpl.processConditions(json, "conditions")) {
            throw new JsonParseException("Skipping loading recipe " + recipeId + " as it's conditions were not met");
        }
    }

}
