package net.threetag.threecore;

import net.minecraftforge.common.ForgeConfigSpec;
import net.threetag.threecore.util.energy.IEnergyConfig;

public class ThreeCoreServerConfig {

    public static Energy ENERGY;

    public static ForgeConfigSpec generateConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ENERGY = new Energy(builder);
        return builder.build();
    }

    // -------------------------------------------------------------------------------

    public static class Energy {

        public EnergyConfig CAPACITOR;
        public EnergyConfig SOLAR_PANEL;
        public ForgeConfigSpec.ConfigValue<Integer> SOLAR_PANEL_PRODUCTION;
        public EnergyConfig ADVANCED_CAPACITOR;
        public EnergyConfig GRINDER;
        public EnergyConfig HYDRAULIC_PRESS;
        public EnergyConfig FLUID_COMPOSER;
        public EnergyConfig STIRLING_GENERATOR;
        public ForgeConfigSpec.ConfigValue<Integer> GOLD_CONDUIT;
        public ForgeConfigSpec.ConfigValue<Integer> COPPER_CONDUIT;
        public ForgeConfigSpec.ConfigValue<Integer> SILVER_CONDUIT;

        private Energy(ForgeConfigSpec.Builder builder) {
            builder.comment("ThreeCore energy settings").push("base.energy");

            CAPACITOR = new EnergyConfig(builder, "capacitor", 2000000, 1000);
            SOLAR_PANEL = new EnergyConfig(builder, "solarPanel", 20000, 20);
            SOLAR_PANEL_PRODUCTION = builder.defineInRange("solarPanel.production", 1, 1, 1000);
            ADVANCED_CAPACITOR = new EnergyConfig(builder, "advancedCapacitor", 8000000, 4000);
            GRINDER = new EnergyConfig(builder, "grinder", 20000, 20);
            HYDRAULIC_PRESS = new EnergyConfig(builder, "hydraulicPress", 20000, 20);
            FLUID_COMPOSER = new EnergyConfig(builder, "fluidComposer", 20000, 20);

            STIRLING_GENERATOR = new EnergyConfig(builder, "stirlingGenerator", 20000, 20);

            GOLD_CONDUIT = builder.defineInRange("goldConduit.transferRate", 128, 1, 10000);
            COPPER_CONDUIT = builder.defineInRange("copperConduit.transferRate", 512, 1, 10000);
            SILVER_CONDUIT = builder.defineInRange("silverConduit.transferRate", 1024, 1, 10000);

            builder.pop();
        }

    }

    public static class EnergyConfig implements IEnergyConfig {

        public ForgeConfigSpec.ConfigValue<Integer> capacity;
        public ForgeConfigSpec.ConfigValue<Integer> power;

        public EnergyConfig(ForgeConfigSpec.Builder builder, String name, int capacity, int power) {
            this.capacity = builder.defineInRange(name + ".capacity", capacity, 100, 10000000);
            this.power = builder.defineInRange(name + ".power", power, 100, 10000000);
        }

        @Override
        public int getCapacity() {
            return capacity.get();
        }

        @Override
        public int getPower() {
            return power.get();
        }
    }

}
