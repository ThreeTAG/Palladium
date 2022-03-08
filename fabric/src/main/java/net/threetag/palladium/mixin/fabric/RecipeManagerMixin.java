package net.threetag.palladium.mixin.fabric;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(at = @At("HEAD"), method = "fromJson")
    private static void fromJson(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<Recipe<?>> callback) {
//        if (!RecipeCondition.processConditions(json, "conditions")) {
//            throw new JsonParseException("Skipping loading recipe " + recipeId + " as it's conditions were not met");
//        }
    }

}
