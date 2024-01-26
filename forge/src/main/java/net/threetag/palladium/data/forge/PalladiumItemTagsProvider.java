package net.threetag.palladium.data.forge;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tags.PalladiumItemTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.threetag.palladium.block.PalladiumBlocks.*;

@SuppressWarnings("unchecked")
public class PalladiumItemTagsProvider extends IntrinsicHolderTagsProvider<Item> {

    public PalladiumItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.ITEM, completableFuture, item -> item.builtInRegistryHolder().key(), Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(PalladiumItemTags.VIBRATION_ABSORPTION_BOOTS).add(PalladiumItems.VIBRANIUM_WEAVE_BOOTS.get());

        // Ore Blocks
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.ORES_LEAD, PalladiumItemTags.Fabric.ORES_LEAD, LEAD_ORE.get(), DEEPSLATE_LEAD_ORE.get());
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.ORES_TITANIUM, PalladiumItemTags.Fabric.ORES_TITANIUM, TITANIUM_ORE.get());
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.ORES_VIBRANIUM, PalladiumItemTags.Fabric.ORES_VIBRANIUM, VIBRANIUM_ORE.get());
        this.multiLoaderTagMetalTags(Tags.Items.ORES, PalladiumItemTags.Forge.ORES_LEAD, PalladiumItemTags.Fabric.ORES, PalladiumItemTags.Fabric.ORES_LEAD);
        this.multiLoaderTagMetalTags(Tags.Items.ORES, PalladiumItemTags.Forge.ORES_TITANIUM, PalladiumItemTags.Fabric.ORES, PalladiumItemTags.Fabric.ORES_TITANIUM);
        this.multiLoaderTagMetalTags(Tags.Items.ORES, PalladiumItemTags.Forge.ORES_VIBRANIUM, PalladiumItemTags.Fabric.ORES, PalladiumItemTags.Fabric.ORES_VIBRANIUM);

        // Raw Ores
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.RAW_LEAD, PalladiumItemTags.Fabric.RAW_LEAD, PalladiumItems.RAW_LEAD.get());
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.RAW_TITANIUM, PalladiumItemTags.Fabric.RAW_TITANIUM, PalladiumItems.RAW_TITANIUM.get());
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.RAW_VIBRANIUM, PalladiumItemTags.Fabric.RAW_VIBRANIUM, PalladiumItems.RAW_VIBRANIUM.get());
        this.multiLoaderTagMetalTags(Tags.Items.RAW_MATERIALS, PalladiumItemTags.Forge.RAW_LEAD, PalladiumItemTags.Fabric.RAW_ORES, PalladiumItemTags.Fabric.RAW_LEAD);
        this.multiLoaderTagMetalTags(Tags.Items.RAW_MATERIALS, PalladiumItemTags.Forge.RAW_TITANIUM, PalladiumItemTags.Fabric.RAW_ORES, PalladiumItemTags.Fabric.RAW_TITANIUM);
        this.multiLoaderTagMetalTags(Tags.Items.RAW_MATERIALS, PalladiumItemTags.Forge.RAW_VIBRANIUM, PalladiumItemTags.Fabric.RAW_ORES, PalladiumItemTags.Fabric.RAW_VIBRANIUM);

        // Raw Ore Blocks
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.RAW_LEAD_BLOCKS, PalladiumItemTags.Fabric.RAW_LEAD_BLOCKS, PalladiumItems.RAW_LEAD_BLOCK.get());
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.RAW_TITANIUM_BLOCKS, PalladiumItemTags.Fabric.RAW_TITANIUM_BLOCKS, PalladiumItems.RAW_TITANIUM_BLOCK.get());
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.RAW_VIBRANIUM_BLOCKS, PalladiumItemTags.Fabric.RAW_VIBRANIUM_BLOCKS, PalladiumItems.RAW_VIBRANIUM_BLOCK.get());
        this.tag(Tags.Items.STORAGE_BLOCKS).addTags(PalladiumItemTags.Forge.RAW_LEAD_BLOCKS, PalladiumItemTags.Forge.RAW_TITANIUM_BLOCKS, PalladiumItemTags.Forge.RAW_VIBRANIUM_BLOCKS);

        // Storage Blocks
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.STORAGE_BLOCKS_LEAD, PalladiumItemTags.Fabric.STORAGE_BLOCKS_LEAD, PalladiumItems.LEAD_BLOCK.get());
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.STORAGE_BLOCKS_VIBRANIUM, PalladiumItemTags.Fabric.STORAGE_BLOCKS_VIBRANIUM, PalladiumItems.VIBRANIUM_BLOCK.get());
        this.tag(Tags.Items.STORAGE_BLOCKS).addTags(PalladiumItemTags.Forge.STORAGE_BLOCKS_LEAD, PalladiumItemTags.Forge.STORAGE_BLOCKS_VIBRANIUM);

        // Ingots
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.INGOTS_LEAD, PalladiumItemTags.Fabric.INGOTS_LEAD, PalladiumItems.LEAD_INGOT.get());
        this.multiLoaderTagMetalItems(PalladiumItemTags.Forge.INGOTS_VIBRANIUM, PalladiumItemTags.Fabric.INGOTS_VIBRANIUM, PalladiumItems.VIBRANIUM_INGOT.get());
        this.multiLoaderTagMetalTags(Tags.Items.INGOTS, PalladiumItemTags.Forge.INGOTS_LEAD, PalladiumItemTags.Fabric.INGOTS, PalladiumItemTags.Fabric.INGOTS_LEAD);
        this.multiLoaderTagMetalTags(Tags.Items.INGOTS, PalladiumItemTags.Forge.INGOTS_VIBRANIUM, PalladiumItemTags.Fabric.INGOTS, PalladiumItemTags.Fabric.INGOTS_VIBRANIUM);

        this.connectTag(PalladiumItemTags.WOODEN_STICKS, Tags.Items.RODS_WOODEN, PalladiumItemTags.Fabric.WOODEN_STICKS);
        this.connectTag(PalladiumItemTags.IRON_INGOTS, Tags.Items.INGOTS_IRON, PalladiumItemTags.Fabric.INGOTS_IRON);
        this.connectTag(PalladiumItemTags.LEAD_INGOTS, PalladiumItemTags.Forge.INGOTS_LEAD, PalladiumItemTags.Fabric.INGOTS_LEAD);
        this.connectTag(PalladiumItemTags.VIBRANIUM_INGOTS, PalladiumItemTags.Forge.INGOTS_LEAD, PalladiumItemTags.Fabric.INGOTS_LEAD);
        this.connectTag(PalladiumItemTags.QUARTZ, Tags.Items.GEMS_QUARTZ, PalladiumItemTags.Fabric.QUARTZ);
        this.connectTag(PalladiumItemTags.GOLD_INGOTS, Tags.Items.INGOTS_GOLD, PalladiumItemTags.Fabric.INGOTS_GOLD);
        this.connectTag(PalladiumItemTags.COPPER_INGOTS, Tags.Items.INGOTS_COPPER, PalladiumItemTags.Fabric.INGOTS_COPPER);
        this.connectTag(PalladiumItemTags.DIAMONDS, Tags.Items.GEMS_DIAMOND, PalladiumItemTags.Fabric.DIAMONDS);
    }

    public void multiLoaderTagMetalItems(TagKey<Item> forgeTag, TagKey<Item> fabricTag, ItemLike... items) {
        for (ItemLike item : items) {
            this.tag(forgeTag).add(item.asItem());
            this.tag(fabricTag).add(item.asItem());
        }
    }

    public void multiLoaderTagMetalTags(TagKey<Item> rootForge, TagKey<Item> ownForge, TagKey<Item> rootFabric, TagKey<Item> ownFabric) {
        this.tag(rootForge).addTag(ownForge);
        this.tag(rootFabric).addTag(ownFabric);
        System.out.println(rootFabric.location() + " <- " + ownFabric.location());
    }

    public void connectTag(TagKey<Item> palladiumTag, TagKey<Item> forgeTag, TagKey<Item> fabricTag) {
        this.tag(palladiumTag).addOptionalTag(forgeTag.location()).addOptionalTag(fabricTag.location());
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
