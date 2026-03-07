package net.threetag.palladium.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;

public class PalladiumBlockTags {

    public static final TagKey<Block> PREVENTS_INTANGIBILITY = tag("prevents_intangibility");

    public static final TagKey<Block> VIBRANIUM_ORES = c("ores/vibranium");
    public static final TagKey<Block> VIBRANIUM_STORAGE_BLOCKS = c("storage_blocks/vibranium");
    public static final TagKey<Block> RAW_VIBRANIUM_STORAGE_BLOCKS = c("storage_blocks/raw_vibranium");

    private static TagKey<Block> tag(String path) {
        return TagKey.create(Registries.BLOCK, Palladium.id(path));
    }

    private static TagKey<Block> c(String path) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("c", path));
    }
}
