package com.threetag.threecore;

import net.minecraftforge.common.ForgeConfigSpec;

public class ThreeCoreCommonConfig {

    public static Materials MATERIALS;

    public static ForgeConfigSpec generateConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        MATERIALS = new Materials(builder);
        return builder.build();
    }

    // -------------------------------------------------------------------------------

    public static class Materials {
        public OreConfig COPPER;
        public OreConfig TIN;
        public OreConfig LEAD;
        public OreConfig SILVER;
        public OreConfig PALLADIUM;
        public OreConfig VIBRANIUM;
        public OreConfig OSMIUM;
        public OreConfig URANIUM;
        public OreConfig TITANIUM;
        public OreConfig IRIDIUM;
        public OreConfig URU;

        private Materials(ForgeConfigSpec.Builder builder) {
            builder.comment("ThreeCore materials settings").push("materials");

            COPPER = makeOreConfig(builder, "copper", 8, 8, 40, 75);
            TIN = makeOreConfig(builder, "tin", 8, 7, 20, 55);
            LEAD = makeOreConfig(builder, "lead", 8, 4, 10, 35);
            SILVER = makeOreConfig(builder, "silver", 8, 4, 5, 30);
            PALLADIUM = makeOreConfig(builder, "palladium", 6, 3, 10, 25);
            VIBRANIUM = makeOreConfig(builder, "vibranium", 3, 1, 0, 16);
            OSMIUM = makeOreConfig(builder, "osmium", 8, 4, 10, 40);
            URANIUM = makeOreConfig(builder, "uranium", 2, 5, 10, 25);
            TITANIUM = makeOreConfig(builder, "titanium", 3, 2, 0, 16);
            IRIDIUM = makeOreConfig(builder, "iridium", 3, 1, 0, 16);
            URU = makeOreConfig(builder, "uru", 3, 1, 0, 16);

            builder.pop();
        }

        public OreConfig makeOreConfig(ForgeConfigSpec.Builder builder, String ore, int defaultSize, int defaultCount, int defaultMinHeight, int defaultMaxHeight) {
            ForgeConfigSpec.ConfigValue<Integer> size = builder.define("ore." + ore + ".size", defaultSize);
            ForgeConfigSpec.ConfigValue<Integer> count = builder.define("ore." + ore + ".count", defaultCount);
            ForgeConfigSpec.ConfigValue<Integer> minHeight = builder.define("ore." + ore + ".minHeight", defaultMinHeight);
            ForgeConfigSpec.ConfigValue<Integer> maxHeight = builder.define("ore." + ore + ".maxHeight", defaultMaxHeight);
            return new OreConfig(size, count, minHeight, maxHeight);
        }

        // -------------------------------------------------------------------------------

        public static class OreConfig {

            public ForgeConfigSpec.ConfigValue<Integer> size;
            public ForgeConfigSpec.ConfigValue<Integer> count;
            public ForgeConfigSpec.ConfigValue<Integer> minHeight;
            public ForgeConfigSpec.ConfigValue<Integer> maxHeight;

            public OreConfig(ForgeConfigSpec.ConfigValue<Integer> size, ForgeConfigSpec.ConfigValue<Integer> count, ForgeConfigSpec.ConfigValue<Integer> minHeight, ForgeConfigSpec.ConfigValue<Integer> maxHeight) {
                this.size = size;
                this.count = count;
                this.minHeight = minHeight;
                this.maxHeight = maxHeight;
            }
        }
    }

}
