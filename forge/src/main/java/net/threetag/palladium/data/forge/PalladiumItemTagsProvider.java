package net.threetag.palladium.data.forge;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tags.PalladiumItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.threetag.palladium.block.PalladiumBlocks.*;

public class PalladiumItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {

    public PalladiumItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.ITEM, completableFuture, item -> item.builtInRegistryHolder().key(), Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(PalladiumItemTags.VIBRATION_ABSORPTION_BOOTS).add(PalladiumItems.VIBRANIUM_WEAVE_BOOTS.get());

        this.multiLoaderTagMetal(PalladiumItemTags.ORES, PalladiumItemTags.Fabric.ORES, PalladiumItemTags.ORES_LEAD, PalladiumItemTags.Fabric.ORES_LEAD, LEAD_ORE.get(), DEEPSLATE_LEAD_ORE.get());
        this.multiLoaderTagMetal(PalladiumItemTags.ORES, PalladiumItemTags.Fabric.ORES, PalladiumItemTags.ORES_TITANIUM, PalladiumItemTags.Fabric.ORES_TITANIUM, TITANIUM_ORE.get());
        this.multiLoaderTagMetal(PalladiumItemTags.ORES, PalladiumItemTags.Fabric.ORES, PalladiumItemTags.ORES_VIBRANIUM, PalladiumItemTags.Fabric.ORES_VIBRANIUM, VIBRANIUM_ORE.get());

        this.multiLoaderTagMetal(PalladiumItemTags.STORAGE_BLOCKS, PalladiumItemTags.Fabric.STORAGE_BLOCKS, PalladiumItemTags.STORAGE_BLOCKS_LEAD, PalladiumItemTags.Fabric.STORAGE_BLOCKS_LEAD, LEAD_BLOCK.get());
        this.multiLoaderTagMetal(PalladiumItemTags.STORAGE_BLOCKS, PalladiumItemTags.Fabric.STORAGE_BLOCKS, PalladiumItemTags.STORAGE_BLOCKS_VIBRANIUM, PalladiumItemTags.Fabric.STORAGE_BLOCKS_VIBRANIUM, VIBRANIUM_BLOCK.get());

        this.multiLoaderTagMetal(PalladiumItemTags.INGOTS, PalladiumItemTags.Fabric.INGOTS, PalladiumItemTags.INGOTS_IRON, PalladiumItemTags.Fabric.INGOTS_IRON, Items.IRON_INGOT);
        this.multiLoaderTagMetal(PalladiumItemTags.INGOTS, PalladiumItemTags.Fabric.INGOTS, PalladiumItemTags.INGOTS_GOLD, PalladiumItemTags.Fabric.INGOTS_GOLD, Items.GOLD_INGOT);
        this.multiLoaderTagMetal(PalladiumItemTags.INGOTS, PalladiumItemTags.Fabric.INGOTS, PalladiumItemTags.INGOTS_COPPER, PalladiumItemTags.Fabric.INGOTS_COPPER, Items.COPPER_INGOT);
        this.multiLoaderTagMetal(PalladiumItemTags.INGOTS, PalladiumItemTags.Fabric.INGOTS, PalladiumItemTags.INGOTS_LEAD, PalladiumItemTags.Fabric.INGOTS_LEAD, PalladiumItems.LEAD_INGOT.get());
        this.multiLoaderTagMetal(PalladiumItemTags.INGOTS, PalladiumItemTags.Fabric.INGOTS, PalladiumItemTags.INGOTS_VIBRANIUM, PalladiumItemTags.Fabric.INGOTS_VIBRANIUM, PalladiumItems.VIBRANIUM_INGOT.get());

        this.multiLoaderTag(PalladiumItemTags.WOODEN_STICKS, PalladiumItemTags.Fabric.WOODEN_STICKS, Items.STICK);
        this.multiLoaderTag(PalladiumItemTags.REDSTONE, PalladiumItemTags.Fabric.REDSTONE, Items.REDSTONE);
        this.multiLoaderTag(PalladiumItemTags.REDSTONE_BLOCK, PalladiumItemTags.Fabric.REDSTONE_BLOCK, Items.REDSTONE_BLOCK);
        this.multiLoaderTag(PalladiumItemTags.QUARTZ, PalladiumItemTags.Fabric.QUARTZ, Items.QUARTZ);
        this.multiLoaderTag(PalladiumItemTags.QUARTZ_BLOCKS, PalladiumItemTags.Fabric.QUARTZ_BLOCKS, Items.QUARTZ_BLOCK);
    }

    public void multiLoaderTagMetal(TagKey<Item> rootForge, TagKey<Item> rootFabric, TagKey<Item> forgeBranch, TagKey<Item> fabricBranch, ItemLike... items) {
        for (ItemLike itemLike : items) {
            this.tag(fabricBranch).add(itemLike.asItem());
        }
        this.tag(forgeBranch).addTag(fabricBranch);
        this.tag(rootFabric).addTag(fabricBranch);
        this.tag(rootForge).addTag(forgeBranch).addTag(rootFabric);
    }

    public void multiLoaderTag(TagKey<Item> forgeTag, TagKey<Item> fabricTag, ItemLike... items) {
        for (ItemLike itemLike : items) {
            this.tag(fabricTag).add(itemLike.asItem());
        }
        this.tag(forgeTag).addTag(fabricTag);
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
