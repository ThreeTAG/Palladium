package net.threetag.palladium.item.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumRecipeSerializers {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Palladium.MOD_ID, Registries.RECIPE_TYPE);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, Registries.RECIPE_SERIALIZER);

    public static final RegistrySupplier<RecipeType<TailoringRecipe>> TAILORING = RECIPE_TYPES.register("tailoring", () -> new RecipeType<>() {
        public String toString() {
            return Palladium.id("tailoring").toString();
        }
    });
    public static final RegistrySupplier<RecipeSerializer<ItemTailoringRecipe>> ITEM_TAILORING = RECIPE_SERIALIZERS.register("item_tailoring", ItemTailoringRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeSerializer<SuitSetTailoringRecipe>> SUIT_SET_TAILORING = RECIPE_SERIALIZERS.register("suit_set_tailoring", SuitSetTailoringRecipe.Serializer::new);

}
