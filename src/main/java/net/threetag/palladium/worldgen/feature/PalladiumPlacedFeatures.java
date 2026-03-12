package net.threetag.palladium.worldgen.feature;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.threetag.palladium.Palladium;

import java.util.List;

public class PalladiumPlacedFeatures {

    public static final ResourceKey<PlacedFeature> METEORITE_STONE_PILLAR = create("meteorite_stone_pillar");
    public static final ResourceKey<PlacedFeature> ORE_BLACKSTONE = create("ore_blackstone");
    public static final ResourceKey<PlacedFeature> ORE_VIBRANIUM_VEIN_SMALL = create("ore_vibranium_vein_small");
    public static final ResourceKey<PlacedFeature> ORE_VIBRANIUM_VEIN_BIG = create("ore_vibranium_vein_big");

    public static final ResourceKey<PlacedFeature> ORE_COAL_UPPER = create("ore_coal_upper");
    public static final ResourceKey<PlacedFeature> ORE_COAL_LOWER = create("ore_coal_lower");

    public static final ResourceKey<PlacedFeature> ORE_IRON_UPPER = create("ore_iron_upper");
    public static final ResourceKey<PlacedFeature> ORE_IRON_MIDDLE = create("ore_iron_middle");
    public static final ResourceKey<PlacedFeature> ORE_IRON_SMALL = create("ore_iron_small");

    public static final ResourceKey<PlacedFeature> ORE_VIBRANIUM = create("ore_vibranium");
    public static final ResourceKey<PlacedFeature> ORE_VIBRANIUM_MEDIUM = create("ore_vibranium_medium");
    public static final ResourceKey<PlacedFeature> ORE_VIBRANIUM_LARGE = create("ore_vibranium_large");
    public static final ResourceKey<PlacedFeature> ORE_VIBRANIUM_BURIED = create("ore_vibranium_buried");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(
                context, METEORITE_STONE_PILLAR, holdergetter.getOrThrow(PalladiumConfiguredFeatures.METEORITE_STONE_PILLAR), CountPlacement.of(40), InSquarePlacement.spread(), PlacementUtils.FULL_RANGE, BiomeFilter.biome()
        );
        PlacementUtils.register(
                context, ORE_BLACKSTONE, holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_BLACKSTONE), commonOrePlacement(20, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))
        );
        PlacementUtils.register(
                context, ORE_VIBRANIUM_VEIN_SMALL, holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_VIBRANIUM_VEIN_SMALL), commonOrePlacement(96, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))
        );
        PlacementUtils.register(
                context, ORE_VIBRANIUM_VEIN_BIG, holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_VIBRANIUM_VEIN_BIG), commonOrePlacement(24, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top()))
        );

        PlacementUtils.register(
                context, ORE_COAL_UPPER, holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_COAL), commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top()))
        );
        PlacementUtils.register(
                context,
                ORE_COAL_LOWER,
                holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_COAL_BURIED),
                commonOrePlacement(20, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192)))
        );

        PlacementUtils.register(
                context,
                ORE_IRON_UPPER,
                holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_IRON),
                commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))
        );
        PlacementUtils.register(
                context,
                ORE_IRON_MIDDLE,
                holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_IRON),
                commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)))
        );
        PlacementUtils.register(
                context, ORE_IRON_SMALL, holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_IRON_SMALL), commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72)))
        );

        PlacementUtils.register(
                context,
                ORE_VIBRANIUM,
                holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_VIBRANIUM_SMALL),
                commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
        PlacementUtils.register(
                context,
                ORE_VIBRANIUM_MEDIUM,
                holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_VIBRANIUM_MEDIUM),
                commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-4)))
        );
        PlacementUtils.register(
                context,
                ORE_VIBRANIUM_LARGE,
                holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_VIBRANIUM_LARGE),
                rareOrePlacement(9, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
        PlacementUtils.register(
                context,
                ORE_VIBRANIUM_BURIED,
                holdergetter.getOrThrow(PalladiumConfiguredFeatures.ORE_VIBRANIUM_BURIED),
                commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
    }

    public static ResourceKey<PlacedFeature> create(String key) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Palladium.id(key));
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }
}
