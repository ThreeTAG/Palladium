package net.threetag.palladium.world;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Palladium.MOD_ID, Registry.FEATURE_REGISTRY);

    public static final RegistrySupplier<UndergroundMeteoriteFeature> UNDERGROUND_VIBRANIUM_METEORITE = FEATURES.register("underground_vibranium_meteorite", () -> new UndergroundMeteoriteFeature(OreConfiguration.CODEC));

}
