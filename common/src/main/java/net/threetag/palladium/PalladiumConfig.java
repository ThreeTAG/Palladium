package net.threetag.palladium;

import net.minecraftforge.common.ForgeConfigSpec;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.power.ability.AbilityReference;

import java.util.List;

public class PalladiumConfig {

    public static class Client {

        public static ForgeConfigSpec.EnumValue<AbilityBarRenderer.Position> ABILITY_BAR_POSITION;
        public static ForgeConfigSpec.BooleanValue ADDON_PACK_DEV_MODE;
        public static ForgeConfigSpec.BooleanValue ACCESSORY_BUTTON;

        public static ForgeConfigSpec generateConfig() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ABILITY_BAR_POSITION = builder.defineEnum("abilityBarPosition", AbilityBarRenderer.Position.BOTTOM_RIGHT);
            ADDON_PACK_DEV_MODE = builder.define("addonPackDevMode", false);
            ACCESSORY_BUTTON = builder.define("accessoryButton", true);
            return builder.build();
        }

    }

    public static class Server {

        public static ForgeConfigSpec.BooleanValue REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> DISABLED_ABILITIES;

        public static ForgeConfigSpec generateConfig() {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            builder.comment("Enabled some hidden/planned content, that is currently not finished or unused");
            REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION = builder.define("worldGen.redstoneFluxCrystalGeneration", true);
            DISABLED_ABILITIES = builder.defineList("general.disabledAbilities", List.of(), o -> AbilityReference.validateFull(o.toString()));
            return builder.build();
        }

        public static boolean isAbilityDisabled(AbilityReference reference) {
            for (String s : DISABLED_ABILITIES.get()) {
                var ref = AbilityReference.fromString(s);

                if (ref.equals(reference)) {
                    return true;
                }
            }

            return false;
        }

    }

}
