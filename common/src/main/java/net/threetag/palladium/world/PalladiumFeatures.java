package net.threetag.palladium.world;

import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;

import java.util.List;

public class PalladiumFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Palladium.MOD_ID, Registry.FEATURE_REGISTRY);

    public static final RegistrySupplier<UndergroundMeteoriteFeature> UNDERGROUND_METEORITE = FEATURES.register("underground_meteorite", () -> new UndergroundMeteoriteFeature(OreConfiguration.CODEC));

    public static Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_LEAD;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_LEAD_SMALL;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_SILVER;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> ORE_SILVER_BURIED;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> CONFIGURED_UNDERGROUND_METEORITE;

    public static Holder<PlacedFeature> PLACED_ORE_LEAD_UPPER;
    public static Holder<PlacedFeature> PLACED_ORE_LEAD_MIDDLE;
    public static Holder<PlacedFeature> PLACED_ORE_LEAD_SMALL;
    public static Holder<PlacedFeature> PLACED_ORE_SILVER_EXTRA;
    public static Holder<PlacedFeature> PLACED_ORE_SILVER;
    public static Holder<PlacedFeature> PLACED_ORE_SILVER_LOWER;
    public static Holder<PlacedFeature> PLACED_UNDERGROUND_METEORITE;

    public static void init() {
        List<OreConfiguration.TargetBlockState> leadTargets = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, PalladiumBlocks.LEAD_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, PalladiumBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> silverTargets = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, PalladiumBlocks.SILVER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, PalladiumBlocks.DEEPSLATE_SILVER_ORE.get().defaultBlockState()));

        ORE_LEAD = FeatureUtils.register(Palladium.MOD_ID + ":ore_lead", Feature.ORE, new OreConfiguration(leadTargets, 9));
        ORE_LEAD_SMALL = FeatureUtils.register(Palladium.MOD_ID + ":ore_lead_small", Feature.ORE, new OreConfiguration(leadTargets, 4));
        ORE_SILVER = FeatureUtils.register("ore_silver", Feature.ORE, new OreConfiguration(silverTargets, 9));
        ORE_SILVER_BURIED = FeatureUtils.register("ore_silver_buried", Feature.ORE, new OreConfiguration(silverTargets, 9, 0.5F));
        CONFIGURED_UNDERGROUND_METEORITE = FeatureUtils.register("underground_meteorite", UNDERGROUND_METEORITE.get(), new OreConfiguration(OreFeatures.NATURAL_STONE, Blocks.BLACKSTONE.defaultBlockState(), 3, 0.5F));

        PLACED_ORE_LEAD_UPPER = PlacementUtils.register(Palladium.MOD_ID + ":ore_lead_upper", ORE_LEAD, commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384))));
        PLACED_ORE_LEAD_MIDDLE = PlacementUtils.register(Palladium.MOD_ID + ":ore_lead_middle", ORE_LEAD, commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56))));
        PLACED_ORE_LEAD_SMALL = PlacementUtils.register(Palladium.MOD_ID + ":ore_lead_small", ORE_LEAD_SMALL, commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72))));
        PLACED_ORE_SILVER_EXTRA = PlacementUtils.register("ore_silver_extra", ORE_SILVER, commonOrePlacement(50, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256))));
        PLACED_ORE_SILVER = PlacementUtils.register("ore_silver", ORE_SILVER_BURIED, commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32))));
        PLACED_ORE_SILVER_LOWER = PlacementUtils.register("ore_silver_lower", ORE_SILVER_BURIED, orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))));
        PLACED_UNDERGROUND_METEORITE = PlacementUtils.register("underground_meteorite", CONFIGURED_UNDERGROUND_METEORITE, RarityFilter.onAverageOnceEvery(24), InSquarePlacement.spread(), HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)), BiomeFilter.biome());

        BiomeModifications.addProperties(biomeContext -> biomeContext.getProperties().getCategory() != Biome.BiomeCategory.NETHER && biomeContext.getProperties().getCategory() != Biome.BiomeCategory.THEEND, (biomeContext, mutable) -> {
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_LEAD_UPPER);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_LEAD_MIDDLE);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_ORE_LEAD_SMALL);
            mutable.getGenerationProperties().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PLACED_UNDERGROUND_METEORITE);
        });
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
