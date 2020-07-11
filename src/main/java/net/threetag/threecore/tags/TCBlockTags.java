package net.threetag.threecore.tags;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;

public class TCBlockTags {

    public static final ITag.INamedTag<Block> STORAGE_BLOCKS = make("forge", "storage_blocks");
    public static final ITag.INamedTag<Block> ORES = make("forge", "ores");

    public static final ITag.INamedTag<Block> COPPER_STORAGE_BLOCKS = make("forge", "storage_blocks/copper");
    public static final ITag.INamedTag<Block> TIN_STORAGE_BLOCKS = make("forge", "storage_blocks/tin");
    public static final ITag.INamedTag<Block> LEAD_STORAGE_BLOCKS = make("forge", "storage_blocks/lead");
    public static final ITag.INamedTag<Block> SILVER_STORAGE_BLOCKS = make("forge", "storage_blocks/silver");
    public static final ITag.INamedTag<Block> PALLADIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/palladium");
    public static final ITag.INamedTag<Block> VIBRANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/vibranium");
    public static final ITag.INamedTag<Block> OSMIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/osmium");
    public static final ITag.INamedTag<Block> URANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/uranium");
    public static final ITag.INamedTag<Block> TITANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/titanium");
    public static final ITag.INamedTag<Block> IRIDIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/iridium");
    public static final ITag.INamedTag<Block> URU_STORAGE_BLOCKS = make("forge", "storage_blocks/uru");
    public static final ITag.INamedTag<Block> BRONZE_STORAGE_BLOCKS = make("forge", "storage_blocks/bronze");
    public static final ITag.INamedTag<Block> INTERTIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/intertium");
    public static final ITag.INamedTag<Block> STEEL_STORAGE_BLOCKS = make("forge", "storage_blocks/steel");
    public static final ITag.INamedTag<Block> GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS = make("forge", "storage_blocks/gold_titanium_alloy");
    public static final ITag.INamedTag<Block> ADAMANTIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/adamantium");

    public static final ITag.INamedTag<Block> COPPER_ORES = make("forge", "ores/copper");
    public static final ITag.INamedTag<Block> TIN_ORES = make("forge", "ores/tin");
    public static final ITag.INamedTag<Block> LEAD_ORES = make("forge", "ores/lead");
    public static final ITag.INamedTag<Block> SILVER_ORES = make("forge", "ores/silver");
    public static final ITag.INamedTag<Block> PALLADIUM_ORES = make("forge", "ores/palladium");
    public static final ITag.INamedTag<Block> VIBRANIUM_ORES = make("forge", "ores/vibranium");
    public static final ITag.INamedTag<Block> OSMIUM_ORES = make("forge", "ores/osmium");
    public static final ITag.INamedTag<Block> URANIUM_ORES = make("forge", "ores/uranium");
    public static final ITag.INamedTag<Block> TITANIUM_ORES = make("forge", "ores/titanium");
    public static final ITag.INamedTag<Block> IRIDIUM_ORES = make("forge", "ores/iridium");
    public static final ITag.INamedTag<Block> URU_ORES = make("forge", "ores/uru");

    public static ITag.INamedTag<Block> make(String name) {
        return BlockTags.makeWrapperTag(name);
    }

    public static ITag.INamedTag<Block> make(String domain, String path) {
        return BlockTags.makeWrapperTag(domain + ":" + path);
    }

}
