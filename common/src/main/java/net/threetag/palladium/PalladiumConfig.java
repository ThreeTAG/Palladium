package net.threetag.palladium;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.threetag.palladium.client.screen.AbilityBarRenderer;
import net.threetag.palladium.power.ability.AbilityReference;

import java.util.Arrays;
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
            REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION = builder.define("worldGen.redstoneFluxCrystalGeneration", true);
            builder.comment("Allows you to disable specific abilities from addons. Structure: 'power_namespace:power_id#ability_key'. The ability_key can be found in the json of a power.");
            DISABLED_ABILITIES = builder.defineListAllowEmpty(Arrays.asList("general", "disabledAbilities"), List::of, o -> true);
            return builder.build();
        }

        public static boolean isAbilityDisabled(AbilityReference reference) {
            for (String s : DISABLED_ABILITIES.get()) {
                if (AbilityReference.validateFull(s)) {
                    var ref = AbilityReference.fromString(s);

                    if (ref.equals(reference)) {
                        return true;
                    }
                } else if (ResourceLocation.isValidResourceLocation(s)) {
                    var powerId = ResourceLocation.tryParse(s);

                    if (reference.getPowerId() != null && reference.getPowerId().equals(powerId)) {
                        return true;
                    }
                } else if (reference.getPowerId() != null && reference.getPowerId().getNamespace().equals(s)) {
                    return true;
                }
            }

            return false;
        }

    }

}
