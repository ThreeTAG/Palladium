package net.threetag.palladium.tags;

import dev.architectury.hooks.tags.TagHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.Palladium;

public class PalladiumBlockTags {

    public static final Tag.Named<Block> ORES = forgeTag("ores");
    public static final Tag.Named<Block> STORAGE_BLOCKS = forgeTag("storage_blocks");

    public static final Tag.Named<Block> ORES_LEAD = forgeTag("ores/lead");
    public static final Tag.Named<Block> ORES_SILVER = forgeTag("ores/silver");
    public static final Tag.Named<Block> ORES_TITANIUM = forgeTag("ores/titanium");
    public static final Tag.Named<Block> ORES_VIBRANIUM = forgeTag("ores/vibranium");

    public static final Tag.Named<Block> STORAGE_BLOCKS_LEAD = forgeTag("storage_blocks/lead");
    public static final Tag.Named<Block> STORAGE_BLOCKS_SILVER = forgeTag("storage_blocks/silver");
    public static final Tag.Named<Block> STORAGE_BLOCKS_TITANIUM = forgeTag("storage_blocks/titanium");
    public static final Tag.Named<Block> STORAGE_BLOCKS_VIBRANIUM = forgeTag("storage_blocks/vibranium");

    public static final Tag.Named<Block> MINEABLE_WITH_HAMNMER = tag("mineable/hammer");

    private static Tag.Named<Block> tag(String path) {
        return tag(Palladium.MOD_ID, path);
    }

    private static Tag.Named<Block> tag(String domain, String path) {
        return TagHooks.optionalBlock(new ResourceLocation(domain, path));
    }

    private static Tag.Named<Block> forgeTag(String path) {
        return tag("forge", path);
    }

    private static Tag.Named<Block> fabricTag(String path) {
        return tag("c", path);
    }

    public static class Fabric {

        public static final Tag.Named<Block> ORES = fabricTag("ores");
        public static final Tag.Named<Block> STORAGE_BLOCKS = fabricTag("storage_blocks");

        public static final Tag.Named<Block> ORES_LEAD = fabricTag("lead_ores");
        public static final Tag.Named<Block> ORES_SILVER = fabricTag("silver_ores");
        public static final Tag.Named<Block> ORES_TITANIUM = fabricTag("titanium_ores");
        public static final Tag.Named<Block> ORES_VIBRANIUM = fabricTag("vibranium_ores");

        public static final Tag.Named<Block> STORAGE_BLOCKS_LEAD = fabricTag("lead_ores");
        public static final Tag.Named<Block> STORAGE_BLOCKS_SILVER = fabricTag("silver_ores");
        public static final Tag.Named<Block> STORAGE_BLOCKS_TITANIUM = fabricTag("titanium_ores");
        public static final Tag.Named<Block> STORAGE_BLOCKS_VIBRANIUM = fabricTag("vibranium_ores");

    }
}
