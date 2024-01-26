package net.threetag.palladium.data.forge;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tags.PalladiumItemTags;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("NullableProblems")
public class PalladiumRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final ImmutableList<ItemLike> LEAD_SMELTABLES = ImmutableList.of(PalladiumItems.RAW_LEAD.get(), PalladiumItems.LEAD_ORE.get(), PalladiumItems.DEEPSLATE_LEAD_ORE.get());
    private static final ImmutableList<ItemLike> VIBRANIUM_SMELTABLES = ImmutableList.of(PalladiumItems.RAW_VIBRANIUM.get(), PalladiumItems.VIBRANIUM_ORE.get());

    public PalladiumRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, PalladiumItems.SUIT_STAND.get()).pattern(" B ").pattern("SBS").pattern("SXS").define('B', PalladiumItemTags.QUARTZ).define('S', Ingredient.of(Blocks.QUARTZ_SLAB, Blocks.SMOOTH_QUARTZ_SLAB)).define('X', Blocks.SMOOTH_STONE_SLAB).unlockedBy(getHasName(Items.ARMOR_STAND), has(Items.ARMOR_STAND)).save(consumer);

        oreSmelting(consumer, LEAD_SMELTABLES, RecipeCategory.MISC, PalladiumItems.LEAD_INGOT.get(), 0.7F, 200, "lead_ingot");
        oreSmelting(consumer, VIBRANIUM_SMELTABLES, RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT.get(), 1F, 600, "vibranium_ingot");

        oreBlasting(consumer, LEAD_SMELTABLES, RecipeCategory.MISC, PalladiumItems.LEAD_INGOT.get(), 0.7F, 100, "lead_ingot");
        oreBlasting(consumer, VIBRANIUM_SMELTABLES, RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT.get(), 1F, 300, "vibranium_ingot");

        nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, RecipeCategory.MISC, PalladiumItems.LEAD_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.LEAD_BLOCK.get(), "lead_ingot_from_lead_block", "lead_ingot");
        nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, RecipeCategory.MISC, PalladiumItems.VIBRANIUM_INGOT.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.VIBRANIUM_BLOCK.get(), "vibranium_ingot_from_vibranium_block", "vibranium_ingot");

        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, PalladiumItems.RAW_LEAD.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.RAW_LEAD_BLOCK.get());
        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, PalladiumItems.RAW_TITANIUM.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.RAW_TITANIUM_BLOCK.get());
        nineBlockStorageRecipes(consumer, RecipeCategory.MISC, PalladiumItems.RAW_VIBRANIUM.get(), RecipeCategory.BUILDING_BLOCKS, PalladiumItems.RAW_VIBRANIUM_BLOCK.get());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.PURPLE_DYE).requires(PalladiumBlocks.HEART_SHAPED_HERB.get()).group("purple_dye").unlockedBy("has_flower", has(PalladiumBlocks.HEART_SHAPED_HERB.get())).save(consumer, new ResourceLocation(Palladium.MOD_ID, "purple_dye_from_heart_shaped_herb"));

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE), Ingredient.of(Items.LEATHER_BOOTS), Ingredient.of(PalladiumItemTags.VIBRANIUM_INGOTS), RecipeCategory.COMBAT, PalladiumItems.VIBRANIUM_WEAVE_BOOTS.get()).unlocks(getHasName(PalladiumItems.VIBRANIUM_INGOT.get()), has(PalladiumItemTags.VIBRANIUM_INGOTS)).save(consumer, Palladium.id("vibranium_weave_boots_smithing"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.LEAD_CIRCUIT.get()).pattern("II").pattern("LL").pattern("GG").define('I', PalladiumItemTags.IRON_INGOTS).define('L', PalladiumItemTags.LEAD_INGOTS).define('G', PalladiumItemTags.GOLD_INGOTS).unlockedBy(getHasName(PalladiumItems.LEAD_INGOT.get()), has(PalladiumItemTags.LEAD_INGOTS)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.QUARTZ_CIRCUIT.get()).pattern("II").pattern("QQ").pattern("CC").define('I', PalladiumItemTags.IRON_INGOTS).define('Q', PalladiumItemTags.QUARTZ).define('C', PalladiumItemTags.COPPER_INGOTS).unlockedBy(getHasName(Items.QUARTZ), has(PalladiumItemTags.QUARTZ)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.VIBRANIUM_CIRCUIT.get()).pattern("II").pattern("VV").pattern("DD").define('I', PalladiumItemTags.IRON_INGOTS).define('V', PalladiumItemTags.VIBRANIUM_INGOTS).define('D', PalladiumItemTags.DIAMONDS).unlockedBy(getHasName(PalladiumItems.VIBRANIUM_INGOT.get()), has(PalladiumItemTags.VIBRANIUM_INGOTS)).save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.LEAD_FLUX_CAPACITOR.get()).pattern("RLR").pattern("GCG").pattern("LRL").define('R', PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).define('L', PalladiumItemTags.LEAD_INGOTS).define('G', PalladiumItemTags.GOLD_INGOTS).define('C', PalladiumItems.LEAD_CIRCUIT.get()).unlockedBy(getHasName(PalladiumItems.LEAD_INGOT.get()), has(PalladiumItemTags.LEAD_INGOTS)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.QUARTZ_FLUX_CAPACITOR.get()).pattern("RQR").pattern("CFC").pattern("QRQ").define('R', PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).define('Q', PalladiumItemTags.QUARTZ).define('C', PalladiumItemTags.COPPER_INGOTS).define('F', PalladiumItems.LEAD_FLUX_CAPACITOR.get()).unlockedBy(getHasName(Items.QUARTZ), has(PalladiumItemTags.QUARTZ)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PalladiumItems.VIBRANIUM_FLUX_CAPACITOR.get()).pattern("RVR").pattern("DFD").pattern("VRV").define('R', PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).define('V', PalladiumItemTags.VIBRANIUM_INGOTS).define('D', PalladiumItemTags.DIAMONDS).define('F', PalladiumItems.QUARTZ_FLUX_CAPACITOR.get()).unlockedBy(getHasName(PalladiumItems.VIBRANIUM_INGOT.get()), has(PalladiumItemTags.VIBRANIUM_INGOTS)).save(consumer);
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> finishedRecipeConsumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTIme, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, ingredients, category, result, experience, cookingTIme, group, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> finishedRecipeConsumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, ingredients, category, result, experience, cookingTime, group, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> cookingSerializer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group, String recipeName) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, result, experience, cookingTime, cookingSerializer).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(finishedRecipeConsumer, Palladium.id(getItemName(result) + recipeName + "_" + getItemName(itemlike)));
        }
    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed) {
        nineBlockStorageRecipes(finishedRecipeConsumer, unpackedCategory, unpacked, packedCategory, packed, getSimpleRecipeName(packed), null, getSimpleRecipeName(unpacked), null);
    }

    protected static void nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String packedName, String packedGroup) {
        nineBlockStorageRecipes(finishedRecipeConsumer, unpackedCategory, unpacked, packedCategory, packed, packedName, packedGroup, getSimpleRecipeName(unpacked), null);
    }

    protected static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String unpackedName, String unpackedGroup) {
        nineBlockStorageRecipes(finishedRecipeConsumer, unpackedCategory, unpacked, packedCategory, packed, getSimpleRecipeName(packed), null, unpackedName, unpackedGroup);
    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer, RecipeCategory unpackedCategory, ItemLike unpacked, RecipeCategory packedCategory, ItemLike packed, String packedName, @javax.annotation.Nullable String packedGroup, String unpackedName, @javax.annotation.Nullable String unpackedGroup) {
        ShapelessRecipeBuilder.shapeless(unpackedCategory, unpacked, 9).requires(packed).group(unpackedGroup).unlockedBy(getHasName(packed), has(packed)).save(finishedRecipeConsumer, Palladium.id(unpackedName));
        ShapedRecipeBuilder.shaped(packedCategory, packed).define('#', unpacked).pattern("###").pattern("###").pattern("###").group(packedGroup).unlockedBy(getHasName(unpacked), has(unpacked)).save(finishedRecipeConsumer, Palladium.id(packedName));
    }
}
