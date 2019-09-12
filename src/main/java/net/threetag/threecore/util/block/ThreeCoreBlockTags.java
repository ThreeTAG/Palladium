package net.threetag.threecore.util.block;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ThreeCoreBlockTags {

    public static final Tag<Block> STORAGE_BLOCKS = make("forge", "storage_blocks");
    public static final Tag<Block> ORES = make("forge", "ores");

    public static final Tag<Block> COPPER_STORAGE_BLOCKS = make("forge", "storage_blocks/copper");
    public static final Tag<Block> TIN_STORAGE_BLOCKS = make("forge", "storage_blocks/tin");
    public static final Tag<Block> LEAD_STORAGE_BLOCKS = make("forge", "storage_blocks/lead");
    public static final Tag<Block> SILVER_STORAGE_BLOCKS = make("forge", "storage_blocks/silver");
    public static final Tag<Block> PALLADIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/palladium");
    public static final Tag<Block> VIBRANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/vibranium");
    public static final Tag<Block> OSMIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/osmium");
    public static final Tag<Block> URANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/uranium");
    public static final Tag<Block> TITANIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/titanium");
    public static final Tag<Block> IRIDIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/iridium");
    public static final Tag<Block> URU_STORAGE_BLOCKS = make("forge", "storage_blocks/uru");
    public static final Tag<Block> BRONZE_STORAGE_BLOCKS = make("forge", "storage_blocks/bronze");
    public static final Tag<Block> INTERTIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/intertium");
    public static final Tag<Block> STEEL_STORAGE_BLOCKS = make("forge", "storage_blocks/steel");
    public static final Tag<Block> GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS = make("forge", "storage_blocks/gold_titanium_alloy");
    public static final Tag<Block> ADAMANTIUM_STORAGE_BLOCKS = make("forge", "storage_blocks/adamantium");

    public static final Tag<Block> COPPER_ORES = make("forge", "ores/copper");
    public static final Tag<Block> TIN_ORES = make("forge", "ores/tin");
    public static final Tag<Block> LEAD_ORES = make("forge", "ores/lead");
    public static final Tag<Block> SILVER_ORES = make("forge", "ores/silver");
    public static final Tag<Block> PALLADIUM_ORES = make("forge", "ores/palladium");
    public static final Tag<Block> VIBRANIUM_ORES = make("forge", "ores/vibranium");
    public static final Tag<Block> OSMIUM_ORES = make("forge", "ores/osmium");
    public static final Tag<Block> URANIUM_ORES = make("forge", "ores/uranium");
    public static final Tag<Block> TITANIUM_ORES = make("forge", "ores/titanium");
    public static final Tag<Block> IRIDIUM_ORES = make("forge", "ores/iridium");
    public static final Tag<Block> URU_ORES = make("forge", "ores/uru");

    public static Tag<Block> make(String name) {
        return new BlockTags.Wrapper(new ResourceLocation(name));
    }

    public static Tag<Block> make(String domain, String path) {
        return new BlockTags.Wrapper(new ResourceLocation(domain, path));
    }

}
