package net.threetag.palladium.world;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

import java.util.List;

public class PalladiumPlacedFeatures {

    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Palladium.MOD_ID, Registry.PLACED_FEATURE_REGISTRY);

    public static final RegistrySupplier<PlacedFeature> ORE_LEAD_UPPER = PLACED_FEATURES.register("ore_lead_upper",
            () -> new PlacedFeature(holder(PalladiumConfiguredFeatures.ORE_LEAD), commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))));
    public static final RegistrySupplier<PlacedFeature> ORE_LEAD_MIDDLE = PLACED_FEATURES.register("ore_lead_middle",
            () -> new PlacedFeature(holder(PalladiumConfiguredFeatures.ORE_LEAD), commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)))));
    public static final RegistrySupplier<PlacedFeature> ORE_LEAD_SMALL = PLACED_FEATURES.register("ore_lead_small",
            () -> new PlacedFeature(holder(PalladiumConfiguredFeatures.ORE_LEAD), commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72)))));

    public static final RegistrySupplier<PlacedFeature> UNDERGROUND_VIBRANIUM_METEORITE = PLACED_FEATURES.register("underground_vibranium_meteorite",
            () -> new PlacedFeature(holder(PalladiumConfiguredFeatures.UNDERGROUND_VIBRANIUM_METEORITE), List.of(RarityFilter.onAverageOnceEvery(24), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64 - 100), VerticalAnchor.aboveBottom(100)))));

    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier2) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier2, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }

    public static Holder<ConfiguredFeature<?, ?>> holder(RegistrySupplier<ConfiguredFeature<?, ?>> configuredFeature) {
        return BuiltinRegistries.CONFIGURED_FEATURE.getOrCreateHolder(ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, configuredFeature.getId())).result().get();
    }

}
