package net.threetag.palladium.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class PalladiumServerConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAX_SUPERPOWER_SETS = BUILDER
            .comment("How many superpowers a player can have at once")
            .defineInRange("max_superpower_sets", 1, 1, 10);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
