package net.threetag.palladium.tags;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;

public class PalladiumBlockTags {

    public static final TagKey<Block> ORES = forgeTag("ores");
    public static final TagKey<Block> STORAGE_BLOCKS = forgeTag("storage_blocks");

    public static final TagKey<Block> ORES_LEAD = forgeTag("ores/lead");
    public static final TagKey<Block> ORES_TITANIUM = forgeTag("ores/titanium");
    public static final TagKey<Block> ORES_VIBRANIUM = forgeTag("ores/vibranium");

    public static final TagKey<Block> STORAGE_BLOCKS_LEAD = forgeTag("storage_blocks/lead");
    public static final TagKey<Block> STORAGE_BLOCKS_VIBRANIUM = forgeTag("storage_blocks/vibranium");

    public static final TagKey<Block> MINEABLE_WITH_HAMNMER = tag("mineable/hammer");

    private static TagKey<Block> tag(String path) {
        return tag(Palladium.MOD_ID, path);
    }

    private static TagKey<Block> tag(String domain, String path) {
        return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(domain, path));
    }

    private static TagKey<Block> forgeTag(String path) {
        return tag("forge", path);
    }

    private static TagKey<Block> fabricTag(String path) {
        return tag("c", path);
    }

    public static class Fabric {

        public static final TagKey<Block> ORES = fabricTag("ores");
        public static final TagKey<Block> STORAGE_BLOCKS = fabricTag("storage_blocks");

        public static final TagKey<Block> ORES_LEAD = fabricTag("lead_ores");
        public static final TagKey<Block> ORES_TITANIUM = fabricTag("titanium_ores");
        public static final TagKey<Block> ORES_VIBRANIUM = fabricTag("vibranium_ores");

        public static final TagKey<Block> STORAGE_BLOCKS_LEAD = fabricTag("lead_ores");
        public static final TagKey<Block> STORAGE_BLOCKS_VIBRANIUM = fabricTag("vibranium_ores");

    }
}
