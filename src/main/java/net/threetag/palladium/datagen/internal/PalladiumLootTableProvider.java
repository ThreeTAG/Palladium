package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PalladiumLootTableProvider extends LootTableProvider {

    public PalladiumLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Collections.emptySet(), List.of(
                new LootTableProvider.SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)
        ), registries);
    }

    public static class BlockLoot extends BlockLootSubProvider {

        protected BlockLoot(HolderLookup.Provider registries) {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            this.dropSelf(PalladiumBlocks.TAILORING_BENCH.get());
            this.dropSelf(PalladiumBlocks.METEORITE_STONE.get());
            this.dropSelf(PalladiumBlocks.METEORITE_BRICKS.get());
            this.add(PalladiumBlocks.METEORITE_VIBRANIUM_VEIN.get(), b -> this.createSilkTouchDispatchTable(
                    b,
                    this.applyExplosionCondition(
                            b,
                            LootItem.lootTableItem(PalladiumItems.VIBRANIUM_NUGGET)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(0F, 1F)))
                                    .when(BonusLevelTableCondition.bonusLevelFlatChance(this.registries.getOrThrow(Enchantments.FORTUNE), 0.1F, 0.14285715F, 0.25F, 1.0F))
                                    .otherwise(LootItem.lootTableItem(PalladiumBlocks.METEORITE_STONE))
                    )
            ));
            this.dropSelf(PalladiumBlocks.METEORITE_VIBRANIUM_ORE.get());
            this.add(PalladiumBlocks.METEORITE_COAL_ORE.get(), b -> this.createOreDrop(b, Items.COAL));
            this.add(PalladiumBlocks.METEORITE_IRON_ORE.get(), b -> this.createOreDrop(b, Items.RAW_IRON));
            this.add(PalladiumBlocks.METEORITE_COPPER_ORE.get(), this::createCopperOreDrops);
            this.add(PalladiumBlocks.METEORITE_GOLD_ORE.get(), b -> this.createOreDrop(b, Items.RAW_GOLD));
            this.add(PalladiumBlocks.METEORITE_REDSTONE_ORE.get(), this::createRedstoneOreDrops);
            this.add(PalladiumBlocks.METEORITE_EMERALD_ORE.get(), b -> this.createOreDrop(b, Items.EMERALD));
            this.add(PalladiumBlocks.METEORITE_LAPIS_ORE.get(), this::createLapisOreDrops);
            this.add(PalladiumBlocks.METEORITE_DIAMOND_ORE.get(), b -> this.createOreDrop(b, Items.DIAMOND));
            this.add(PalladiumBlocks.METEORITE_VIBRANIUM_ORE.get(), b -> this.createOreDrop(b, PalladiumItems.RAW_VIBRANIUM.get()));
            this.dropSelf(PalladiumBlocks.VIBRANIUM_BLOCK.get());
            this.dropSelf(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return PalladiumBlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toSet());
        }
    }
}
