package net.threetag.palladium.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.registry.DeferredRegistry;
import net.threetag.palladium.registry.RegistrySupplier;

import java.util.List;

public class PalladiumFeatures {

    public static final DeferredRegistry<Feature<?>> FEATURES = DeferredRegistry.create(Palladium.MOD_ID, Registry.FEATURE_REGISTRY);

    public static final RegistrySupplier<UndergroundMeteoriteFeature> UNDERGROUND_METEORITE = FEATURES.register("underground_meteorite", () -> new UndergroundMeteoriteFeature(OreConfiguration.CODEC));

    public static final RuleTest REDSTONE_ORE_REPLACEABLES = new BlockMatchTest(Blocks.REDSTONE_ORE);
    public static final RuleTest DEEPSLATE_REDSTONE_ORE_REPLACEABLES = new BlockMatchTest(Blocks.DEEPSLATE_REDSTONE_ORE);

    public static Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_LEAD;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_LEAD_SMALL;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_SILVER;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_SILVER_BURIED;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> FLUX_CRYSTAL_GEODE;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> CONFIGURED_UNDERGROUND_METEORITE;

    public static Holder<PlacedFeature> PLACED_ORE_LEAD_UPPER;
    public static Holder<PlacedFeature> PLACED_ORE_LEAD_MIDDLE;
    public static Holder<PlacedFeature> PLACED_ORE_LEAD_SMALL;
    public static Holder<PlacedFeature> PLACED_ORE_SILVER_EXTRA;
    public static Holder<PlacedFeature> PLACED_ORE_SILVER;
    public static Holder<PlacedFeature> PLACED_ORE_SILVER_LOWER;
    public static Holder<PlacedFeature> PLACED_FLUX_CRYSTAL_GEODE;
    public static Holder<PlacedFeature> PLACED_UNDERGROUND_METEORITE;

    public static void init() {
        List<OreConfiguration.TargetBlockState> leadTargets = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, PalladiumBlocks.LEAD_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, PalladiumBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> silverTargets = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, PalladiumBlocks.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, PalladiumBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> fluxCrystalTargets = List.of(OreConfiguration.target(REDSTONE_ORE_REPLACEABLES, PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE.get().defaultBlockState()), OreConfiguration.target(DEEPSLATE_REDSTONE_ORE_REPLACEABLES, PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get().defaultBlockState()));

        ORE_LEAD = register("ore_lead", Feature.ORE, new OreConfiguration(leadTargets, 9));
        ORE_LEAD_SMALL = register("ore_lead_small", Feature.ORE, new OreConfiguration(leadTargets, 4));
        ORE_SILVER = register("ore_silver", Feature.ORE, new OreConfiguration(silverTargets, 9));
        ORE_SILVER_BURIED = register("ore_silver_buried", Feature.ORE, new OreConfiguration(silverTargets, 9, 0.5F));
        FLUX_CRYSTAL_GEODE = register("flux_crystal_geode", Feature.ORE, new OreConfiguration(fluxCrystalTargets, 12));
        CONFIGURED_UNDERGROUND_METEORITE = register("underground_meteorite", UNDERGROUND_METEORITE.get(), new OreConfiguration(OreFeatures.NATURAL_STONE, Blocks.BLACKSTONE.defaultBlockState(), 3, 0.5F));

        PLACED_ORE_LEAD_UPPER = register("ore_lead_upper", ORE_LEAD, commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))));
        PLACED_ORE_LEAD_MIDDLE = register("ore_lead_middle", ORE_LEAD, commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
        PLACED_ORE_LEAD_SMALL = register("ore_lead_small", ORE_LEAD_SMALL, commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))));
        PLACED_ORE_SILVER_EXTRA = register("ore_silver_extra", ORE_SILVER, commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256))));
        PLACED_ORE_SILVER = register("ore_silver", ORE_SILVER_BURIED, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
        PLACED_ORE_SILVER_LOWER = register("ore_silver_lower", ORE_SILVER_BURIED, orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));
        PLACED_FLUX_CRYSTAL_GEODE = register("flux_crystal_geode", FLUX_CRYSTAL_GEODE, commonOrePlacement(120, HeightRangePlacement.triangle(VerticalAnchor.absolute(-90), VerticalAnchor.absolute(90))));
        PLACED_UNDERGROUND_METEORITE = register("underground_meteorite", CONFIGURED_UNDERGROUND_METEORITE, RarityFilter.onAverageOnceEvery(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)), BiomeFilter.biome());

        // TODO turn into json?
        BiomeModifications.addProperties(biomeContext -> true, (biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_LEAD_UPPER);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_LEAD_MIDDLE);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_LEAD_SMALL);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_FLUX_CRYSTAL_GEODE);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_UNDERGROUND_METEORITE);
        });
    }

    public static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(String string, F feature, FC featureConfiguration) {
        return FeatureUtils.register(Palladium.MOD_ID + ":" + string, feature, featureConfiguration);
    }

    public static Holder<PlacedFeature> register(String string, Holder<? extends ConfiguredFeature<?, ?>> holder, List<PlacementModifier> list) {
        return PlacementUtils.register(Palladium.MOD_ID + ":" + string, holder, list);
    }

    public static Holder<PlacedFeature> register(String string, Holder<? extends ConfiguredFeature<?, ?>> holder, PlacementModifier... placementModifiers) {
        return PlacementUtils.register(Palladium.MOD_ID + ":" + string, holder, placementModifiers);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier placementModifier, PlacementModifier placementModifier2) {
        return List.of(placementModifier, InSquarePlacement.spread(), placementModifier2, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int i, PlacementModifier placementModifier) {
        return orePlacement(CountPlacement.of(i), placementModifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int i, PlacementModifier placementModifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(i), placementModifier);
    }

}
