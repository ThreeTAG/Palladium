package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tag.PalladiumItemTags;

import java.util.concurrent.CompletableFuture;

public class PalladiumRecipeProvider extends FabricRecipeProvider {

    public PalladiumRecipeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
        return new RecipeProvider(registryLookup, exporter) {
            @Override
            public void buildRecipes() {
                this.shaped(RecipeCategory.DECORATIONS, PalladiumItems.SUIT_STAND.get()).pattern(" B ").pattern("SBS").pattern("SXS").define('B', PalladiumItemTags.QUARTZ).define('S', Ingredient.of(Blocks.QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ_SLAB)).define('X', Blocks.SMOOTH_STONE_SLAB).unlockedBy(getHasName(Items.ARMOR_STAND), has(Items.ARMOR_STAND)).save(exporter);
            }
        };
    }

    @Override
    public String getName() {
        return "Palladium Recipes";
    }
}
