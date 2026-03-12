package net.threetag.palladium.datagen.internal;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.criterion.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.flag.PalladiumFeatureFlags;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tag.PalladiumItemTags;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PalladiumRecipeProvider extends RecipeProvider {

    public static final ImmutableList<ItemLike> VIBRANIUM_SMELTABLES = ImmutableList.of(PalladiumItems.RAW_VIBRANIUM, PalladiumItems.METEORITE_VIBRANIUM_ORE);

    public PalladiumRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.shaped(RecipeCategory.DECORATIONS, PalladiumItems.SUIT_STAND.get()).pattern(" B ").pattern("SBS").pattern("SXS").define('B', Tags.Items.GEMS_QUARTZ).define('S', Ingredient.of(Blocks.QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ_SLAB)).define('X', Blocks.SMOOTH_STONE_SLAB).unlockedBy(getHasName(Items.ARMOR_STAND), has(Items.ARMOR_STAND)).save(this.output);
        this.shaped(RecipeCategory.DECORATIONS, PalladiumItems.TAILORING_BENCH.get()).pattern("SF").pattern("WW").define('S', Tags.Items.TOOLS_SHEAR).define('F', PalladiumItemTags.FABRICS).define('W', ItemTags.PLANKS).unlockedBy("unlock_right_away", PlayerTrigger.TriggerInstance.tick()).showNotification(false).save(this.output.withConditions(PalladiumFeatureFlags.conditionTailoring()));

        // Materials
        this.shaped(RecipeCategory.BUILDING_BLOCKS, PalladiumBlocks.METEORITE_BRICKS, 4)
                .define('S', PalladiumBlocks.METEORITE_STONE)
                .pattern("SS")
                .pattern("SS")
                .unlockedBy(getHasName(PalladiumBlocks.METEORITE_STONE), this.has(PalladiumBlocks.METEORITE_STONE))
                .save(this.output);
        this.stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, PalladiumBlocks.METEORITE_BRICKS, PalladiumBlocks.METEORITE_STONE);
        this.nineBlockStorageRecipes(RecipeCategory.MISC, PalladiumItems.RAW_VIBRANIUM, RecipeCategory.BUILDING_BLOCKS, PalladiumItems.RAW_VIBRANIUM_BLOCK);
        this.nineBlockStorageRecipesRecipesWithCustomUnpacking(
                RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT, RecipeCategory.BUILDING_BLOCKS, PalladiumItems.VIBRANIUM_BLOCK, Palladium.id("vibranium_ingot_from_vibranium_block").toString(), "vibranium_ingot"
        );
        this.nineBlockStorageRecipesWithCustomPacking(
                RecipeCategory.MISC, PalladiumItems.VIBRANIUM_NUGGET, RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT, Palladium.id("vibranium_ingot_vibranium_nuggets").toString(), "vibranium_ingot"
        );
        this.oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, Collections.singletonList(PalladiumItems.METEORITE_COAL_ORE), RecipeCategory.MISC, Items.COAL, 0.1F, 200, "coal", "_from_smelting");
        this.oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, Collections.singletonList(PalladiumItems.METEORITE_IRON_ORE), RecipeCategory.MISC, Items.IRON_INGOT, 0.7F, 200, "iron_ingot", "_from_smelting");
        this.oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, Collections.singletonList(PalladiumItems.METEORITE_COPPER_ORE), RecipeCategory.MISC, Items.COPPER_INGOT, 0.7F, 200, "copper_ingot", "_from_smelting");
        this.oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, Collections.singletonList(PalladiumItems.METEORITE_GOLD_ORE), RecipeCategory.MISC, Items.GOLD_INGOT, 1F, 200, "gold_ingot", "_from_smelting");
        this.oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, Collections.singletonList(PalladiumItems.METEORITE_DIAMOND_ORE), RecipeCategory.MISC, Items.DIAMOND, 1F, 200, "diamond", "_from_smelting");
        this.oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, Collections.singletonList(PalladiumItems.METEORITE_LAPIS_ORE), RecipeCategory.MISC, Items.LAPIS_LAZULI, 0.2F, 200, "lapis_lazuli", "_from_smelting");
        this.oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, Collections.singletonList(PalladiumItems.METEORITE_REDSTONE_ORE), RecipeCategory.MISC, Items.REDSTONE, 0.7F, 200, "redstone", "_from_smelting");
        this.oreCooking(RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, Collections.singletonList(PalladiumItems.METEORITE_EMERALD_ORE), RecipeCategory.MISC, Items.EMERALD, 1F, 200, "emerald", "_from_smelting");
        this.oreSmelting(VIBRANIUM_SMELTABLES, RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT, 1.5F, 400, "vibranium_ingot");

        // Fabrics
        for (DyeColor color : DyeColor.values()) {
            this.shaped(RecipeCategory.MISC, PalladiumItems.FABRIC_BY_COLOR.get(color).get(), 8).pattern(" WS").pattern("WXW").pattern("SW ").define('W', getWoolBlockByColor(color)).define('S', Tags.Items.RODS_WOODEN).define('X', Tags.Items.STRINGS).unlockedBy(getHasName(DyeItem.byColor(color)), has(PalladiumItemTags.DYE_BY_COLOR.get(color))).group("fabrics").save(this.output.withConditions(PalladiumFeatureFlags.conditionTailoring()));
            this.shapeless(RecipeCategory.MISC, PalladiumItems.FABRIC_BY_COLOR.get(color).get()).requires(PalladiumItemTags.FABRICS).requires(PalladiumItemTags.DYE_BY_COLOR.get(color)).unlockedBy(getHasName(DyeItem.byColor(color)), has(PalladiumItemTags.DYE_BY_COLOR.get(color))).group("dye_fabrics").save(this.output.withConditions(PalladiumFeatureFlags.conditionTailoring()), Palladium.id("fabric_recoloring_" + color.getName()).toString());
            this.shapeless(RecipeCategory.MISC, PalladiumItems.FABRIC_BY_COLOR.get(color).get(), 8).requires(Ingredient.of(this.items.getOrThrow(PalladiumItemTags.FABRICS)), 8).requires(PalladiumItemTags.DYE_BY_COLOR.get(color)).unlockedBy(getHasName(DyeItem.byColor(color)), has(PalladiumItemTags.DYE_BY_COLOR.get(color))).group("dye_fabrics").save(this.output.withConditions(PalladiumFeatureFlags.conditionTailoring()), Palladium.id("fabric_recoloring_8_" + color.getName()).toString());
        }
    }

    private static Item getWoolBlockByColor(DyeColor color) {
        return BuiltInRegistries.ITEM.getValue(Identifier.withDefaultNamespace(color.getName() + "_wool"));
    }

    @Override
    protected void nineBlockStorageRecipes(RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed) {
        this.nineBlockStorageRecipes(unpackedCategory, unpacked, packedCategory, packed, Palladium.MOD_ID + ":" + getSimpleRecipeName(packed), null, Palladium.MOD_ID + ":" + getSimpleRecipeName(unpacked), null);
    }

    @Override
    protected void nineBlockStorageRecipesRecipesWithCustomUnpacking(
            RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String unpackedName, String unpackedGroup
    ) {
        this.nineBlockStorageRecipes(unpackedCategory, unpacked, packedCategory, packed, Palladium.MOD_ID + ":" + getSimpleRecipeName(packed), null, unpackedName, unpackedGroup);
    }

    @Override
    protected void nineBlockStorageRecipesWithCustomPacking(
            RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String packedName, String packedGroup
    ) {
        this.nineBlockStorageRecipes(unpackedCategory, unpacked, packedCategory, packed, packedName, packedGroup, Palladium.MOD_ID + ":" + getSimpleRecipeName(unpacked), null);
    }

    @Override
    protected void stonecutterResultFromBase(RecipeCategory category, ItemLike result, ItemLike material, int resultCount) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(material), category, result, resultCount)
                .unlockedBy(getHasName(material), this.has(material))
                .save(this.output, Palladium.MOD_ID + ":" + getConversionRecipeName(result, material) + "_stonecutting");
    }

    @Override
    protected <T extends AbstractCookingRecipe> void oreCooking(
            RecipeSerializer<T> serializer,
            AbstractCookingRecipe.Factory<T> recipeFactory,
            List<ItemLike> ingredients,
            RecipeCategory category,
            ItemLike result,
            float experience,
            int cookingTime,
            String group,
            String suffix
    ) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, result, experience, cookingTime, serializer, recipeFactory)
                    .group(group)
                    .unlockedBy(getHasName(itemlike), this.has(itemlike))
                    .save(this.output, Palladium.MOD_ID + ":" + getItemName(result) + suffix + "_" + getItemName(itemlike));
        }
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
