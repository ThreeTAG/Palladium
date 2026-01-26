package net.threetag.palladium.data.forge;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladiumcore.registry.RegistrySupplier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class PalladiumLootTableProvider extends LootTableProvider {

    public PalladiumLootTableProvider(PackOutput output) {
        super(output, BuiltInLootTables.all(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK),
                new LootTableProvider.SubProviderEntry(ChestLoot::new, LootContextParamSets.CHEST)
        ));
    }

    public static class BlockLoot extends BlockLootSubProvider {

        protected BlockLoot() {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            this.add(PalladiumBlocks.LEAD_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_LEAD.get()));
            this.add(PalladiumBlocks.DEEPSLATE_LEAD_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_LEAD.get()));
            this.add(PalladiumBlocks.TITANIUM_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_TITANIUM.get()));
            this.add(PalladiumBlocks.VIBRANIUM_ORE.get(), (block) -> createOreDrop(block, PalladiumItems.RAW_VIBRANIUM.get()));
            this.add(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE.get(), (block) -> createSilkTouchDispatchTable(block, (LootItem.lootTableItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
            this.add(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get(), (block) -> createSilkTouchDispatchTable(block, (LootItem.lootTableItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES)))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
            this.add(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER.get(), (block) -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(4.0F))).apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE)).when(MatchTool.toolMatches(net.minecraft.advancements.critereon.ItemPredicate.Builder.item().of(ItemTags.CLUSTER_MAX_HARVESTABLES))).otherwise(applyExplosionDecay(block, LootItem.lootTableItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))))));
            this.dropWhenSilkTouch(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get());
            this.dropWhenSilkTouch(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get());
            this.dropWhenSilkTouch(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get());
            this.dropSelf(PalladiumBlocks.LEAD_BLOCK.get());
            this.dropSelf(PalladiumBlocks.TITANIUM_BLOCK.get());
            this.dropSelf(PalladiumBlocks.VIBRANIUM_BLOCK.get());
            this.dropSelf(PalladiumBlocks.RAW_LEAD_BLOCK.get());
            this.dropSelf(PalladiumBlocks.RAW_TITANIUM_BLOCK.get());
            this.dropSelf(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());
            this.dropSelf(PalladiumBlocks.HEART_SHAPED_HERB.get());
            this.dropSelf(PalladiumBlocks.TAILORING_BENCH.get());
            this.dropPottedContents(PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get());
            this.dropSelf(PalladiumBlocks.MULTIVERSAL_ITERATOR.get());
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return PalladiumBlocks.BLOCKS.getEntries().stream().map(RegistrySupplier::get).toList();
        }
    }

    public static class ChestLoot implements LootTableSubProvider {

        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
            output.accept(Palladium.id("chests/ruined_multiverse_portal"),
                    LootTable.lootTable()
                            .withPool(
                                    LootPool.lootPool()
                                            .setRolls(ConstantValue.exactly(1.0F))
                                            .add(LootItem.lootTableItem(PalladiumItems.MULTIVERSAL_EXTRAPOLATOR.get()))
                            )
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(3.0F))
                                    .add(LootItem.lootTableItem(PalladiumItems.VIBRANIUM_CIRCUIT.get()))
                                    .add(LootItem.lootTableItem(PalladiumItems.VIBRANIUM_FLUX_CAPACITOR.get()))
                                    .add(LootItem.lootTableItem(PalladiumItems.RAW_VIBRANIUM.get()).setWeight(3))
                                    .add(LootItem.lootTableItem(PalladiumItems.QUARTZ_CIRCUIT.get()).setWeight(6))
                                    .add(LootItem.lootTableItem(PalladiumItems.QUARTZ_FLUX_CAPACITOR.get()).setWeight(6))
                                    .add(LootItem.lootTableItem(PalladiumItems.RAW_TITANIUM.get()).setWeight(9))
                                    .add(LootItem.lootTableItem(PalladiumItems.LEAD_CIRCUIT.get()).setWeight(12))
                                    .add(LootItem.lootTableItem(PalladiumItems.LEAD_FLUX_CAPACITOR.get()).setWeight(12))
                                    .add(LootItem.lootTableItem(PalladiumItems.RAW_LEAD.get()).setWeight(15))
                            )
                            .withPool(LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(5F))
                                    .add(LootItem.lootTableItem(PalladiumItems.REDSTONE_FLUX_CRYSTAL.get()).setWeight(5))
                                    .add(LootItem.lootTableItem(Items.REDSTONE).setWeight(5))
                                    .add(LootItem.lootTableItem(Items.OBSIDIAN).setWeight(2))
                                    .add(LootItem.lootTableItem(Items.REDSTONE_BLOCK))
                                    .add(LootItem.lootTableItem(PalladiumItems.HEART_SHAPED_HERB.get()))
                            ));
        }
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, @NotNull ValidationContext context) {
        map.forEach((id, table) -> table.validate(context.setParams(table.getParamSet()).enterElement("{" + id + "}", new LootDataId<>(LootDataType.TABLE, id))));
    }
}
