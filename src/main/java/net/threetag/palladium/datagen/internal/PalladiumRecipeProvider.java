package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tag.PalladiumItemTags;

import java.util.concurrent.CompletableFuture;

public class PalladiumRecipeProvider extends RecipeProvider {

    public PalladiumRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.shaped(RecipeCategory.DECORATIONS, PalladiumItems.SUIT_STAND.get()).pattern(" B ").pattern("SBS").pattern("SXS").define('B', PalladiumItemTags.QUARTZ).define('S', Ingredient.of(Blocks.QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ_SLAB)).define('X', Blocks.SMOOTH_STONE_SLAB).unlockedBy(getHasName(Items.ARMOR_STAND), has(Items.ARMOR_STAND)).save(this.output);
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
            return new PalladiumRecipeProvider(provider, output);
        }

        @Override
        public String getName() {
            return "Palladium Recipes";
        }
    }

}
