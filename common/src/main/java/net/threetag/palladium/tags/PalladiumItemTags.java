package net.threetag.palladium.tags;

import dev.architectury.hooks.tags.TagHooks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;

public class PalladiumItemTags {

    public static final Tag.Named<Item> ORES = forgeTag("ores");
    public static final Tag.Named<Item> STORAGE_BLOCKS = forgeTag("storage_blocks");
    public static final Tag.Named<Item> INGOTS = forgeTag("ingots");
    public static final Tag.Named<Item> WOODEN_STICKS = forgeTag("rods/wooden");
    public static final Tag.Named<Item> REDSTONE = forgeTag("dusts/redstone");
    public static final Tag.Named<Item> REDSTONE_BLOCK = forgeTag("storage_blocks/redstone");
    public static final Tag.Named<Item> QUARTZ = forgeTag("gems/quartz");
    public static final Tag.Named<Item> QUARTZ_BLOCKS = forgeTag("storage_blocks/quartz");

    public static final Tag.Named<Item> ORES_LEAD = forgeTag("ores/lead");
    public static final Tag.Named<Item> ORES_SILVER = forgeTag("ores/silver");
    public static final Tag.Named<Item> ORES_TITANIUM = forgeTag("ores/titanium");
    public static final Tag.Named<Item> ORES_VIBRANIUM = forgeTag("ores/vibranium");

    public static final Tag.Named<Item> STORAGE_BLOCKS_LEAD = forgeTag("storage_blocks/lead");
    public static final Tag.Named<Item> STORAGE_BLOCKS_SILVER = forgeTag("storage_blocks/silver");
    public static final Tag.Named<Item> STORAGE_BLOCKS_TITANIUM = forgeTag("storage_blocks/titanium");
    public static final Tag.Named<Item> STORAGE_BLOCKS_VIBRANIUM = forgeTag("storage_blocks/vibranium");

    public static final Tag.Named<Item> INGOTS_IRON = forgeTag("ingots/iron");
    public static final Tag.Named<Item> INGOTS_GOLD = forgeTag("ingots/gold");
    public static final Tag.Named<Item> INGOTS_COPPER = forgeTag("ingots/copper");
    public static final Tag.Named<Item> INGOTS_LEAD = forgeTag("ingots/lead");
    public static final Tag.Named<Item> INGOTS_SILVER = forgeTag("ingots/silver");
    public static final Tag.Named<Item> INGOTS_TITANIUM = forgeTag("ingots/titanium");
    public static final Tag.Named<Item> INGOTS_VIBRANIUM = forgeTag("ingots/vibranium");

    private static Tag.Named<Item> tag(String domain, String path) {
        return TagHooks.optionalItem(new ResourceLocation(domain, path));
    }

    private static Tag.Named<Item> forgeTag(String path) {
        return tag("forge", path);
    }

    private static Tag.Named<Item> fabricTag(String path) {
        return tag("c", path);
    }

    public static class Fabric {

        public static final Tag.Named<Item> ORES = fabricTag("ores");
        public static final Tag.Named<Item> STORAGE_BLOCKS = fabricTag("storage_blocks");
        public static final Tag.Named<Item> INGOTS = fabricTag("ingots");
        public static final Tag.Named<Item> WOODEN_STICKS = fabricTag("wood_sticks");
        public static final Tag.Named<Item> REDSTONE = fabricTag("redstone_dusts");
        public static final Tag.Named<Item> REDSTONE_BLOCK = fabricTag("redstone_blocks");
        public static final Tag.Named<Item> QUARTZ = fabricTag("quartz");
        public static final Tag.Named<Item> QUARTZ_BLOCKS = fabricTag("quartz_blocks");

        public static final Tag.Named<Item> ORES_LEAD = fabricTag("lead_ores");
        public static final Tag.Named<Item> ORES_SILVER = fabricTag("silver_ores");
        public static final Tag.Named<Item> ORES_TITANIUM = fabricTag("titanium_ores");
        public static final Tag.Named<Item> ORES_VIBRANIUM = fabricTag("vibranium_ores");

        public static final Tag.Named<Item> STORAGE_BLOCKS_LEAD = fabricTag("lead_blocks");
        public static final Tag.Named<Item> STORAGE_BLOCKS_SILVER = fabricTag("silver_blocks");
        public static final Tag.Named<Item> STORAGE_BLOCKS_TITANIUM = fabricTag("titanium_blocks");
        public static final Tag.Named<Item> STORAGE_BLOCKS_VIBRANIUM = fabricTag("vibranium_blocks");

        public static final Tag.Named<Item> INGOTS_IRON = fabricTag("iron_ingots");
        public static final Tag.Named<Item> INGOTS_GOLD = fabricTag("gold_ingots");
        public static final Tag.Named<Item> INGOTS_COPPER = fabricTag("copper_ingots");
        public static final Tag.Named<Item> INGOTS_LEAD = fabricTag("lead_ingots");
        public static final Tag.Named<Item> INGOTS_SILVER = fabricTag("silver_ingots");
        public static final Tag.Named<Item> INGOTS_TITANIUM = fabricTag("titanium_ingots");
        public static final Tag.Named<Item> INGOTS_VIBRANIUM = fabricTag("vibranium_ingots");

    }

}
