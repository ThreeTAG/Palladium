package net.threetag.palladium;

import net.minecraftforge.common.ForgeConfigSpec;
import net.threetag.palladium.client.screen.AbilityBarRenderer;

public class PalladiumConfig {

    public static class Client {

        public static ForgeConfigSpec.EnumValue<AbilityBarRenderer.Position> ABILITY_BAR_POSITION;

        public static ForgeConfigSpec generateConfig() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ABILITY_BAR_POSITION = builder.defineEnum("abilityBarPosition", AbilityBarRenderer.Position.BOTTOM_RIGHT);
            return builder.build();
        }

    }

    @ExpectPlatform
    public static boolean addonPackDevMode() {
        throw new AssertionError();
    }

}
