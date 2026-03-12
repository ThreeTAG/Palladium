package net.threetag.palladium.worldgen.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

public class PalladiumFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Palladium.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> METEORITE_STONE_PILLAR = FEATURES.register("meteorite_stone_pillar", () -> new MeteoriteStonePillarFeature(NoneFeatureConfiguration.CODEC));

}
