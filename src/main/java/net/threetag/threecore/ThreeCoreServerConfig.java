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
        public EnergyConfig ADVANCED_CAPACITOR;
        public EnergyConfig GRINDER;
        public EnergyConfig HYDRAULIC_PRESS;
        public EnergyConfig FLUID_COMPOSER;

        private Energy(ForgeConfigSpec.Builder builder) {
            builder.comment("ThreeCore energy settings").push("base.energy");

            CAPACITOR = new EnergyConfig(builder, "capacitor", 2000000, 1000);
            // TODO energy values
            SOLAR_PANEL = new EnergyConfig(builder, "solarPanel", 20000, 20);
            ADVANCED_CAPACITOR = new EnergyConfig(builder, "advancedCapacitor", 8000000, 4000);
            GRINDER = new EnergyConfig(builder, "grinder", 20000, 20);
            HYDRAULIC_PRESS = new EnergyConfig(builder, "hydraulicPress", 20000, 20);
            FLUID_COMPOSER = new EnergyConfig(builder, "fluidComposer", 20000, 20);

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
