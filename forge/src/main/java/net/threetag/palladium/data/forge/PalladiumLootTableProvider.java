package net.threetag.palladium.data.forge;

import net.minecraft.data.DataGenerator;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;

public class PalladiumLootTableProvider extends BlockLootTableProvider {

    public PalladiumLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    public void addTables() {
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
        this.dropSelf(PalladiumBlocks.VIBRANIUM_BLOCK.get());
        this.dropSelf(PalladiumBlocks.RAW_LEAD_BLOCK.get());
        this.dropSelf(PalladiumBlocks.RAW_TITANIUM_BLOCK.get());
        this.dropSelf(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());
        this.dropSelf(PalladiumBlocks.HEART_SHAPED_HERB.get());
        this.dropPottedContents(PalladiumBlocks.POTTED_HEART_SHAPED_HERB.get());
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
