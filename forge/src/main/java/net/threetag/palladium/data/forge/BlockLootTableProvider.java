package net.threetag.palladium.data.forge;

import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.threetag.palladium.Palladium;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class BlockLootTableProvider extends LootTableProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item().hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));
    private static final LootItemCondition.Builder HAS_NO_SILK_TOUCH = HAS_SILK_TOUCH.invert();
    private static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));
    private static final LootItemCondition.Builder HAS_SHEARS_OR_SILK_TOUCH = HAS_SHEARS.or(HAS_SILK_TOUCH);
    private static final LootItemCondition.Builder HAS_NO_SHEARS_OR_SILK_TOUCH = HAS_SHEARS_OR_SILK_TOUCH.invert();
    public static final Set<Item> EXPLOSION_RESISTANT = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemLike::asItem).collect(ImmutableSet.toImmutableSet());
    private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
    private static final float[] JUNGLE_LEAVES_SAPLING_CHANGES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};
    public final Map<ResourceLocation, LootTable.Builder> lootTables = new HashMap<>();
    private final DataGenerator generator;

    public BlockLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
        this.generator = dataGeneratorIn;
    }

    public abstract void addTables();

    @Override
    public void run(HashCache cache) {
        addTables();

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<ResourceLocation, LootTable.Builder> entry : lootTables.entrySet()) {
            tables.put(entry.getKey(), entry.getValue().setParamSet(LootContextParamSets.BLOCK).build());
        }
        writeTables(cache, tables);
    }

    private void writeTables(HashCache cache, Map<ResourceLocation, LootTable> tables) {
        Path outputFolder = this.generator.getOutputFolder();
        tables.forEach((key, lootTable) -> {
            Path path = outputFolder.resolve("data/" + key.getNamespace() + "/loot_tables/" + key.getPath() + ".json");
            try {
                DataProvider.save(GSON, cache, LootTables.serialize(lootTable), path);
            } catch (IOException e) {
                Palladium.LOGGER.error("Couldn't write loot table {}", path, e);
            }
        });
    }

    public static <T> T applyExplosionDecay(ItemLike pItem, FunctionUserBuilder<T> pFunction) {
        return !EXPLOSION_RESISTANT.contains(pItem.asItem()) ? pFunction.apply(ApplyExplosionDecay.explosionDecay()) : pFunction.unwrap();
    }

    public static <T> T applyExplosionCondition(ItemLike pItem, ConditionUserBuilder<T> pCondition) {
        return !EXPLOSION_RESISTANT.contains(pItem.asItem()) ? pCondition.when(ExplosionCondition.survivesExplosion()) : pCondition.unwrap();
    }

    public static LootTable.Builder createSingleItemTable(ItemLike p_124127_) {
        return LootTable.lootTable().withPool(applyExplosionCondition(p_124127_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124127_))));
    }

    /**
     * If the condition from {@code conditionBuilder} succeeds, drops 1 {@code block}.
     * Otherwise, drops loot specified by {@code alternativeEntryBuilder}.
     */
    public static LootTable.Builder createSelfDropDispatchTable(Block pBlock, LootItemCondition.Builder pConditionBuilder, LootPoolEntryContainer.Builder<?> pAlternativeEntryBuilder) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pBlock).when(pConditionBuilder).otherwise(pAlternativeEntryBuilder)));
    }

    /**
     * If the block is mined with Silk Touch, drops 1 {@code block}.
     * Otherwise, drops loot specified by {@code alternativeEntryBuilder}.
     */
    public static LootTable.Builder createSilkTouchDispatchTable(Block pBlock, LootPoolEntryContainer.Builder<?> pAlternativeEntryBuilder) {
        return createSelfDropDispatchTable(pBlock, HAS_SILK_TOUCH, pAlternativeEntryBuilder);
    }

    /**
     * If the block is mined with Shears, drops 1 {@code block}.
     * Otherwise, drops loot specified by {@code alternativeEntryBuilder}.
     */
    public static LootTable.Builder createShearsDispatchTable(Block pBlock, LootPoolEntryContainer.Builder<?> pAlternativeEntryBuilder) {
        return createSelfDropDispatchTable(pBlock, HAS_SHEARS, pAlternativeEntryBuilder);
    }

    /**
     * If the block is mined either with Silk Touch or Shears, drops 1 {@code block}.
     * Otherwise, drops loot specified by {@code alternativeEntryBuilder}.
     */
    public static LootTable.Builder createSilkTouchOrShearsDispatchTable(Block pBlock, LootPoolEntryContainer.Builder<?> pAlternativeEntryBuilder) {
        return createSelfDropDispatchTable(pBlock, HAS_SHEARS_OR_SILK_TOUCH, pAlternativeEntryBuilder);
    }

    public static LootTable.Builder createSingleItemTableWithSilkTouch(Block pSilkTouchBlock, ItemLike pNoSilkTouchItemLike) {
        return createSilkTouchDispatchTable(pSilkTouchBlock, applyExplosionCondition(pSilkTouchBlock, LootItem.lootTableItem(pNoSilkTouchItemLike)));
    }

    public static LootTable.Builder createSingleItemTable(ItemLike pItemLike, NumberProvider pDropCountProvider) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(applyExplosionDecay(pItemLike, LootItem.lootTableItem(pItemLike).apply(SetItemCountFunction.setCount(pDropCountProvider)))));
    }

    public static LootTable.Builder createSingleItemTableWithSilkTouch(Block pSilkTouchBlock, ItemLike pNoSilkTouchItemLike, NumberProvider pDropCountProvider) {
        return createSilkTouchDispatchTable(pSilkTouchBlock, applyExplosionDecay(pSilkTouchBlock, LootItem.lootTableItem(pNoSilkTouchItemLike).apply(SetItemCountFunction.setCount(pDropCountProvider))));
    }

    public static LootTable.Builder createSilkTouchOnlyTable(ItemLike pItem) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pItem)));
    }

    public static LootTable.Builder createPotFlowerItemTable(ItemLike pFlowerPotContent) {
        return LootTable.lootTable().withPool(applyExplosionCondition(Blocks.FLOWER_POT, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Blocks.FLOWER_POT)))).withPool(applyExplosionCondition(pFlowerPotContent, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pFlowerPotContent))));
    }

    public static LootTable.Builder createSlabItemTable(Block p_124291_) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(applyExplosionDecay(p_124291_, LootItem.lootTableItem(p_124291_).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_124291_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))))));
    }

    public static <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTable(Block pBlock, Property<T> pProperty, T pValue) {
        return LootTable.lootTable().withPool(applyExplosionCondition(pBlock, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pBlock).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(pProperty, pValue))))));
    }

    public static LootTable.Builder createNameableBlockEntityTable(Block p_124293_) {
        return LootTable.lootTable().withPool(applyExplosionCondition(p_124293_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124293_).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
    }

//    public static LootTable.Builder createEnergyBlockEntityTable(Block p_124293_) {
//        return LootTable.lootTable().withPool(applyExplosionCondition(p_124293_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124293_).apply(CopyEnergyFunction.builder(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
//    }
//
//    public static LootTable.Builder createNameableAndEnergyBlockEntityTable(Block p_124293_) {
//        return LootTable.lootTable().withPool(applyExplosionCondition(p_124293_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124293_).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyEnergyFunction.builder(CopyNameFunction.NameSource.BLOCK_ENTITY)))));
//    }

    public static LootTable.Builder createShulkerBoxDrop(Block p_124295_) {
        return LootTable.lootTable().withPool(applyExplosionCondition(p_124295_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124295_).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Lock", "BlockEntityTag.Lock").copy("LootTable", "BlockEntityTag.LootTable").copy("LootTableSeed", "BlockEntityTag.LootTableSeed")).apply(SetContainerContents.setContents(BlockEntityType.SHULKER_BOX).withEntry(DynamicLoot.dynamicEntry(ShulkerBoxBlock.CONTENTS))))));
    }

    public static LootTable.Builder createCopperOreDrops(Block p_176047_) {
        return createSilkTouchDispatchTable(p_176047_, applyExplosionDecay(p_176047_, LootItem.lootTableItem(Items.RAW_COPPER).apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    public static LootTable.Builder createLapisOreDrops(Block p_176049_) {
        return createSilkTouchDispatchTable(p_176049_, applyExplosionDecay(p_176049_, LootItem.lootTableItem(Items.LAPIS_LAZULI).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 9.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    public static LootTable.Builder createRedstoneOreDrops(Block p_176051_) {
        return createSilkTouchDispatchTable(p_176051_, applyExplosionDecay(p_176051_, LootItem.lootTableItem(Items.REDSTONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(4.0F, 5.0F))).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    public static LootTable.Builder createBannerDrop(Block p_124297_) {
        return LootTable.lootTable().withPool(applyExplosionCondition(p_124297_, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124297_).apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY)).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Patterns", "BlockEntityTag.Patterns")))));
    }

    public static LootTable.Builder createBeeNestDrop(Block p_124299_) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124299_).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Bees", "BlockEntityTag.Bees")).apply(CopyBlockState.copyState(p_124299_).copy(BeehiveBlock.HONEY_LEVEL))));
    }

    public static LootTable.Builder createBeeHiveDrop(Block p_124301_) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(p_124301_).when(HAS_SILK_TOUCH).apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY).copy("Bees", "BlockEntityTag.Bees")).apply(CopyBlockState.copyState(p_124301_).copy(BeehiveBlock.HONEY_LEVEL)).otherwise(LootItem.lootTableItem(p_124301_))));
    }

    public static LootTable.Builder createCaveVinesDrop(Block p_176053_) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.GLOW_BERRIES)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176053_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CaveVines.BERRIES, true))));
    }

    public static LootTable.Builder createOreDrop(Block pBlock, Item pItem) {
        return createSilkTouchDispatchTable(pBlock, applyExplosionDecay(pBlock, LootItem.lootTableItem(pItem).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))));
    }

    /**
     * Creates a builder that drops the given IItemProvider in amounts between 0 and 2, most often 0. Only used in
     * vanilla for huge mushroom blocks.
     */
    public static LootTable.Builder createMushroomBlockDrop(Block pBlock, ItemLike pItem) {
        return createSilkTouchDispatchTable(pBlock, applyExplosionDecay(pBlock, LootItem.lootTableItem(pItem).apply(SetItemCountFunction.setCount(UniformGenerator.between(-6.0F, 2.0F))).apply(LimitCount.limitCount(IntRange.lowerBound(0)))));
    }

    public static LootTable.Builder createGrassDrops(Block p_124303_) {
        return createShearsDispatchTable(p_124303_, applyExplosionDecay(p_124303_, LootItem.lootTableItem(Items.WHEAT_SEEDS).when(LootItemRandomChanceCondition.randomChance(0.125F)).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))));
    }

    /**
     * Creates a builder that drops the given IItemProvider in amounts between 0 and 3, based on the AGE property. Only
     * used in vanilla for pumpkin and melon stems.
     */
    public static LootTable.Builder createStemDrops(Block pStemBlock, Item pSeedsItem) {
        return LootTable.lootTable().withPool(applyExplosionDecay(pStemBlock, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pSeedsItem).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.06666667F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pStemBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, 0)))).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.13333334F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pStemBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, 1)))).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.2F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pStemBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, 2)))).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.26666668F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pStemBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, 3)))).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.33333334F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pStemBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, 4)))).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.4F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pStemBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, 5)))).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.46666667F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pStemBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, 6)))).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.53333336F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pStemBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(StemBlock.AGE, 7)))))));
    }

    public static LootTable.Builder createAttachedStemDrops(Block pAttachedStemBlock, Item pSeedsItem) {
        return LootTable.lootTable().withPool(applyExplosionDecay(pAttachedStemBlock, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pSeedsItem).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.53333336F))))));
    }

    public static LootTable.Builder createShearsOnlyDrop(ItemLike p_124287_) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_SHEARS).add(LootItem.lootTableItem(p_124287_)));
    }

    public static LootTable.Builder createGlowLichenDrops(Block p_176055_) {
        return LootTable.lootTable().withPool(LootPool.lootPool().add(applyExplosionDecay(p_176055_, LootItem.lootTableItem(p_176055_).when(HAS_SHEARS).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176055_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PipeBlock.EAST, true)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176055_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PipeBlock.WEST, true)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176055_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PipeBlock.NORTH, true)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176055_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PipeBlock.SOUTH, true)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176055_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PipeBlock.UP, true)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F), true).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176055_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PipeBlock.DOWN, true)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(-1.0F), true)))));
    }

    /**
     * Used for all leaves, drops self with silk touch, otherwise drops the second Block param with the passed chances
     * for fortune levels, adding in sticks.
     */
    public static LootTable.Builder createLeavesDrops(Block pLeavesBlock, Block pSaplingBlock, float... pChances) {
        return createSilkTouchOrShearsDispatchTable(pLeavesBlock, applyExplosionCondition(pLeavesBlock, LootItem.lootTableItem(pSaplingBlock)).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, pChances))).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_NO_SHEARS_OR_SILK_TOUCH).add(applyExplosionDecay(pLeavesBlock, LootItem.lootTableItem(Items.STICK).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
    }

    /**
     * Used for oak and dark oak, same as droppingWithChancesAndSticks but adding in apples.
     */
    public static LootTable.Builder createOakLeavesDrops(Block pOakLeavesBlock, Block pSaplingBlock, float... pChances) {
        return createLeavesDrops(pOakLeavesBlock, pSaplingBlock, pChances).withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).when(HAS_NO_SHEARS_OR_SILK_TOUCH).add(applyExplosionCondition(pOakLeavesBlock, LootItem.lootTableItem(Items.APPLE)).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.005F, 0.0055555557F, 0.00625F, 0.008333334F, 0.025F))));
    }

    /**
     * If {@code dropGrownCropCondition} fails (i.e. crop is not ready), drops 1 {@code seedsItem}.
     * If {@code dropGrownCropCondition} succeeds (i.e. crop is ready), drops 1 {@code grownCropItem}, and 0-3 {@code
     * seedsItem} with fortune applied.
     */
    public static LootTable.Builder createCropDrops(Block pCropBlock, Item pGrownCropItem, Item pSeedsItem, LootItemCondition.Builder pDropGrownCropCondition) {
        return applyExplosionDecay(pCropBlock, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(pGrownCropItem).when(pDropGrownCropCondition).otherwise(LootItem.lootTableItem(pSeedsItem)))).withPool(LootPool.lootPool().when(pDropGrownCropCondition).add(LootItem.lootTableItem(pSeedsItem).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.5714286F, 3)))));
    }

    public static LootTable.Builder createDoublePlantShearsDrop(Block pSheared) {
        return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SHEARS).add(LootItem.lootTableItem(pSheared).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
    }

    public static LootTable.Builder createDoublePlantWithSeedDrops(Block pBlock, Block pSheared) {
        LootPoolEntryContainer.Builder<?> builder = LootItem.lootTableItem(pSheared).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))).when(HAS_SHEARS).otherwise(applyExplosionCondition(pBlock, LootItem.lootTableItem(Items.WHEAT_SEEDS)).when(LootItemRandomChanceCondition.randomChance(0.125F)));
        return LootTable.lootTable().withPool(LootPool.lootPool().add(builder).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER))).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).build()).build()), new BlockPos(0, 1, 0)))).withPool(LootPool.lootPool().add(builder).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))).when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(pBlock).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).build()).build()), new BlockPos(0, -1, 0))));
    }

    public static LootTable.Builder createCandleDrops(Block p_176057_) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(applyExplosionDecay(p_176057_, LootItem.lootTableItem(p_176057_).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176057_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CandleBlock.CANDLES, 2)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176057_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CandleBlock.CANDLES, 3)))).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F)).when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(p_176057_).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CandleBlock.CANDLES, 4)))))));
    }

    public static LootTable.Builder createCandleCakeDrops(Block pCandleCakeBlock) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pCandleCakeBlock)));
    }

    private void addNetherVinesDropTable(Block pVines, Block pPlant) {
        LootTable.Builder loottable$builder = createSilkTouchOrShearsDispatchTable(pVines, LootItem.lootTableItem(pVines).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.33F, 0.55F, 0.77F, 1.0F)));
        this.add(pVines, loottable$builder);
        this.add(pPlant, loottable$builder);
    }

    public static LootTable.Builder createDoorTable(Block p_124138_) {
        return createSinglePropConditionTable(p_124138_, DoorBlock.HALF, DoubleBlockHalf.LOWER);
    }

    public Iterable<Block> getKnownBlocks() {
        return Registry.BLOCK;
    }

    public void dropPottedContents(Block pFlowerPot) {
        this.add(pFlowerPot, (p_176061_) -> {
            return createPotFlowerItemTable(((FlowerPotBlock)p_176061_).getContent());
        });
    }

    public void otherWhenSilkTouch(Block pBlock, Block pSilkTouchDrop) {
        this.add(pBlock, createSilkTouchOnlyTable(pSilkTouchDrop));
    }

    public void dropOther(Block pBlock, ItemLike pDrop) {
        this.add(pBlock, createSingleItemTable(pDrop));
    }

    public void dropWhenSilkTouch(Block pBlock) {
        this.otherWhenSilkTouch(pBlock, pBlock);
    }

    public void dropSelf(Block pBlock) {
        this.dropOther(pBlock, pBlock);
    }

    public void add(Block pBlock, Function<Block, LootTable.Builder> pFactory) {
        this.add(pBlock, pFactory.apply(pBlock));
    }

    public void add(Block pBlock, LootTable.Builder pLootTableBuilder) {
        this.lootTables.put(pBlock.getLootTable(), pLootTableBuilder);
    }

}
