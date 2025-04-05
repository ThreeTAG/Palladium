package net.threetag.palladium;

import eu.midnightdust.lib.config.MidnightConfig;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.gui.screen.abilitybar.AbilityKeyBindDisplay;

public class PalladiumConfig extends MidnightConfig {

    public static final String CATEGORY_CLIENT = "client";
    public static final String CATEGORY_GAMEPLAY = "gameplay";

    @Entry(category = CATEGORY_CLIENT)
    public static UiAlignment ABILITY_BAR_ALIGNMENT = UiAlignment.TOP_LEFT;

    @Entry(category = CATEGORY_CLIENT)
    public static AbilityKeyBindDisplay ABILITY_BAR_KEY_BIND_DISPLAY = AbilityKeyBindDisplay.INSIDE;

    @Entry(category = CATEGORY_CLIENT)
    public static boolean HIDE_EXPERIMENTAL_WARNING = true;

    @Entry(category = CATEGORY_GAMEPLAY)
    public static int MAX_SUPERPOWER_SETS = 1;

}
