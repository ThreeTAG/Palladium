package net.threetag.palladium.world;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.threetag.palladium.Palladium;

import java.util.List;

public class PalladiumPlacedFeatures {

    public static final ResourceKey<PlacedFeature> ORE_LEAD_UPPER = createKey("ore_lead_upper");
    public static final ResourceKey<PlacedFeature> ORE_LEAD_MIDDLE = createKey("ore_lead_middle");
    public static final ResourceKey<PlacedFeature> ORE_LEAD_SMALL = createKey("ore_lead_small");
    public static final ResourceKey<PlacedFeature> UNDERGROUND_VIBRANIUM_METEORITE = createKey("underground_vibranium_meteorite");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, ORE_LEAD_UPPER, configuredFeatures.getOrThrow(PalladiumConfiguredFeatures.ORE_LEAD),
                commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))));

        register(context, ORE_LEAD_MIDDLE, configuredFeatures.getOrThrow(PalladiumConfiguredFeatures.ORE_LEAD),
                commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));

        register(context, ORE_LEAD_SMALL, configuredFeatures.getOrThrow(PalladiumConfiguredFeatures.ORE_LEAD),
                commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))));

        register(context, UNDERGROUND_VIBRANIUM_METEORITE, configuredFeatures.getOrThrow(PalladiumConfiguredFeatures.UNDERGROUND_VIBRANIUM_METEORITE),
                List.of(RarityFilter.onAverageOnceEvery(24), InSquarePlacement.spread(), HeightRangePlacement.triangle(VerticalAnchor.absolute(-64 - 100), VerticalAnchor.aboveBottom(100))));
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier2) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier2, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }

    private static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Palladium.id(name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }

}
