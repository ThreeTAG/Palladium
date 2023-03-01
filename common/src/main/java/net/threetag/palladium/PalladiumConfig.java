package net.threetag.palladium;

import net.minecraftforge.common.ForgeConfigSpec;
import net.threetag.palladium.client.screen.AbilityBarRenderer;

public class PalladiumConfig {

    public static class Client {

        public static ForgeConfigSpec.EnumValue<AbilityBarRenderer.Position> ABILITY_BAR_POSITION;
        public static ForgeConfigSpec.BooleanValue ADDON_PACK_DEV_MODE;

        public static ForgeConfigSpec generateConfig() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ABILITY_BAR_POSITION = builder.defineEnum("abilityBarPosition", AbilityBarRenderer.Position.BOTTOM_RIGHT);
            ADDON_PACK_DEV_MODE = builder.define("addonPackDevMode", false);
            return builder.build();
        }

    }

    public static class Server {

        public static ForgeConfigSpec.BooleanValue REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION;
        public static ForgeConfigSpec.BooleanValue EXPERIMENTAL_FEATURES;

        public static ForgeConfigSpec generateConfig() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.comment("Enabled some hidden/planned content, that is currently not finished or unused");
            EXPERIMENTAL_FEATURES = builder.define("general.experimentalFeatures", false);
            REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION = builder.define("worldGen.redstoneFluxCrystalGeneration", true);
            return builder.build();
        }

    }

}
