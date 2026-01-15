package net.threetag.palladium.item.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

public class PalladiumRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Palladium.MOD_ID);

    public static final DeferredHolder<RecipeSerializer<?>, ItemTailoringRecipe.Serializer> ITEM_TAILORING = RECIPE_SERIALIZERS.register("item_tailoring", ItemTailoringRecipe.Serializer::new);

}
