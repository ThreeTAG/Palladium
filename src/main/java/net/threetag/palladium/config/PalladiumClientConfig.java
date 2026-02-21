package net.threetag.palladium.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityBarAlignment;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityKeyBindDisplay;

public class PalladiumClientConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue DEV_MODE = BUILDER
            .comment("Enabled additional utilities for addon developers")
            .define("dev_mode", true);

    public static final ModConfigSpec.EnumValue<AbilityBarAlignment> ABILITY_BAR_ALIGNMENT = BUILDER
            .comment("Position of the ability bar on your screen")
            .defineEnum("ability_bar_alignment", AbilityBarAlignment.TOP_LEFT);

    public static final ModConfigSpec.EnumValue<AbilityKeyBindDisplay> ABILITY_BAR_KEY_BIND_DISPLAY = BUILDER
            .comment("Whether to position the keybinds in the ability inside or outside of the icons")
            .defineEnum("ability_bar_key_bind_display", AbilityKeyBindDisplay.INSIDE);

    public static final ModConfigSpec.BooleanValue HIDE_EXPERIMENTAL_WARNING = BUILDER
            .comment("Whether to remove the experimental feature warning upon opening worlds")
            .define("hide_experimental_warning", true);

    public static final ModConfigSpec.BooleanValue SCALE_CAMERA_FIX = BUILDER
            .comment("This options fixes the weird camera movements when being small")
            .define("scale_camera_fix", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
