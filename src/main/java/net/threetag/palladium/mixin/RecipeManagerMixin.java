package net.threetag.palladium.mixin;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.threetag.palladium.item.recipe.PalladiumRecipePropertySets;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Shadow
    @Final
    @Mutable
    private static Map<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> RECIPE_PROPERTY_SETS;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void addPropertySets(HolderLookup.Provider registries, CallbackInfo ci) {
        RECIPE_PROPERTY_SETS = new HashMap<>(RECIPE_PROPERTY_SETS);
        PalladiumRecipePropertySets.addPropertySets(RECIPE_PROPERTY_SETS);
    }

}
