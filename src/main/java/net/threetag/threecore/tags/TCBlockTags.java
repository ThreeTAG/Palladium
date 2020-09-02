package net.threetag.threecore.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class TCBlockTags {

    public static final Tags.IOptionalNamedTag<Block> COPPER_STORAGE_BLOCKS = make("forge", "storage_blocks/copper");
    public static final Tags.IOptionalNamedTag<Block> TIN_STORAGE_BLOCKS = make("forge", "storage_blocks/tin");
    public static final Tags.IOptionalNamedTag<Block> LEAD_STORAGE_BLOCKS = make("forge", "storage_blocks/lead");
    public static final Tags.IOptionalNamedTag<Block> SILVER_STORAGE_BLOCKS = make("forge", "storage_blocks/silver");
    public static final Tags.IOptionalNamedTag<Block> PALLADIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/palladium");
    public static final Tags.IOptionalNamedTag<Block> VIBRANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/vibranium");
    public static final Tags.IOptionalNamedTag<Block> OSMIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/osmium");
    public static final Tags.IOptionalNamedTag<Block> URANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/uranium");
    public static final Tags.IOptionalNamedTag<Block> TITANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/titanium");
    public static final Tags.IOptionalNamedTag<Block> IRIDIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/iridium");
    public static final Tags.IOptionalNamedTag<Block> URU_STORAGE_BLOCKS = make("forge", "storage_blocks/uru");
    public static final Tags.IOptionalNamedTag<Block> BRONZE_STORAGE_BLOCKS = make("forge", "storage_blocks/bronze");
    public static final Tags.IOptionalNamedTag<Block> INTERTIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/intertium");
    public static final Tags.IOptionalNamedTag<Block> STEEL_STORAGE_BLOCKS = make("forge", "storage_blocks/steel");
    public static final Tags.IOptionalNamedTag<Block> GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS = make("forge", "storage_blocks/gold_titanium_alloy");
    public static final Tags.IOptionalNamedTag<Block> ADAMANTIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/adamantium");

    public static final Tags.IOptionalNamedTag<Block> COPPER_ORES = make("forge", "ores/copper");
    public static final Tags.IOptionalNamedTag<Block> TIN_ORES = make("forge", "ores/tin");
    public static final Tags.IOptionalNamedTag<Block> LEAD_ORES = make("forge", "ores/lead");
    public static final Tags.IOptionalNamedTag<Block> SILVER_ORES = make("forge", "ores/silver");
    public static final Tags.IOptionalNamedTag<Block> PALLADIUM_ORES = make("forge", "ores/palladium");
    public static final Tags.IOptionalNamedTag<Block> VIBRANIUM_ORES = make("forge", "ores/vibranium");
    public static final Tags.IOptionalNamedTag<Block> OSMIUM_ORES = make("forge", "ores/osmium");
    public static final Tags.IOptionalNamedTag<Block> URANIUM_ORES = make("forge", "ores/uranium");
    public static final Tags.IOptionalNamedTag<Block> TITANIUM_ORES = make("forge", "ores/titanium");
    public static final Tags.IOptionalNamedTag<Block> IRIDIUM_ORES = make("forge", "ores/iridium");
    public static final Tags.IOptionalNamedTag<Block> URU_ORES = make("forge", "ores/uru");

    public static Tags.IOptionalNamedTag<Block> make(String domain, String path) {
        return BlockTags.createOptional(new ResourceLocation(domain, path));
    }

}
