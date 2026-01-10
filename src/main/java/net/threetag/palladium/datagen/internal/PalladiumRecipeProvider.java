package net.threetag.palladium.datagen.internal;

import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tag.PalladiumItemTags;

import java.util.concurrent.CompletableFuture;

public class PalladiumRecipeProvider extends RecipeProvider {

    public PalladiumRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.shaped(RecipeCategory.DECORATIONS, PalladiumItems.SUIT_STAND.get()).pattern(" B ").pattern("SBS").pattern("SXS").define('B', Tags.Items.GEMS_QUARTZ).define('S', Ingredient.of(Blocks.QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ_SLAB)).define('X', Blocks.SMOOTH_STONE_SLAB).unlockedBy(getHasName(Items.ARMOR_STAND), has(Items.ARMOR_STAND)).save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, PalladiumItems.TAILORING_BENCH.get()).pattern("SF").pattern("WW").define('S', Tags.Items.TOOLS_SHEAR).define('F', PalladiumItemTags.FABRICS).define('W', ItemTags.PLANKS).unlockedBy("unlock_right_away", PlayerTrigger.TriggerInstance.tick()).showNotification(false).save(this.output);

        // Fabrics
        for (DyeColor color : DyeColor.values()) {
            this.shaped(RecipeCategory.MISC, PalladiumItems.FABRIC_BY_COLOR.get(color).get(), 8).pattern(" WS").pattern("WXW").pattern("SW ").define('W', getWoolBlockByColor(color)).define('S', Tags.Items.RODS_WOODEN).define('X', Tags.Items.STRINGS).unlockedBy(getHasName(DyeItem.byColor(color)), has(PalladiumItemTags.DYE_BY_COLOR.get(color))).group("fabrics").save(this.output);
            this.shapeless(RecipeCategory.MISC, PalladiumItems.FABRIC_BY_COLOR.get(color).get()).requires(PalladiumItemTags.FABRICS).requires(PalladiumItemTags.DYE_BY_COLOR.get(color)).unlockedBy(getHasName(DyeItem.byColor(color)), has(PalladiumItemTags.DYE_BY_COLOR.get(color))).group("dye_fabrics").save(this.output, Palladium.id("fabric_recoloring_" + color.getName()).toString());
            this.shapeless(RecipeCategory.MISC, PalladiumItems.FABRIC_BY_COLOR.get(color).get(), 8).requires(Ingredient.of(this.items.getOrThrow(PalladiumItemTags.FABRICS)), 8).requires(PalladiumItemTags.DYE_BY_COLOR.get(color)).unlockedBy(getHasName(DyeItem.byColor(color)), has(PalladiumItemTags.DYE_BY_COLOR.get(color))).group("dye_fabrics").save(this.output, Palladium.id("fabric_recoloring_8_" + color.getName()).toString());
        }
    }

    private static Item getWoolBlockByColor(DyeColor color) {
        return BuiltInRegistries.ITEM.getValue(ResourceLocation.withDefaultNamespace(color.getName() + "_wool"));
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
