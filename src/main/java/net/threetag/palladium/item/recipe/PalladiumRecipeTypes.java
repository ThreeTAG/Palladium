package net.threetag.palladium.item.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class PalladiumRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Palladium.MOD_ID);

    public static final DeferredHolder<RecipeType<?>, RecipeType<TailoringRecipe>> TAILORING = create("tailoring");

    private static <T extends Recipe<?>> DeferredHolder<RecipeType<?>, RecipeType<T>> create(String path) {
        return RECIPE_TYPES.register(path, () -> new RecipeType<>() {
            @Override
            public String toString() {
                return Palladium.id(path).toString();
            }
        });
    }

    @SubscribeEvent
    static void datapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(TAILORING.get());
    }
}
