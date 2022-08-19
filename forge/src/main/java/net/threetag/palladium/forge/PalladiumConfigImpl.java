package net.threetag.palladium.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.threetag.palladium.client.screen.AbilityBarRenderer;

public class PalladiumConfigImpl {

    public static void init() {
        // Client
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Client.generateConfig());
    }

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

    public static AbilityBarRenderer.Position getAbilityBarPosition() {
        return Client.ABILITY_BAR_POSITION.get();
    }

    public static boolean addonPackDevMode() {
        return Client.ADDON_PACK_DEV_MODE.get();
    }
}
