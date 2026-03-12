package net.threetag.palladium.worldgen.feature;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;

import java.util.List;

public class PalladiumConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> METEORITE_STONE_PILLAR = create("meteorite_stone_pillar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_BLACKSTONE = create("ore_blackstone");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VIBRANIUM_VEIN_SMALL = create("ore_vibranium_vein_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VIBRANIUM_VEIN_BIG = create("ore_vibranium_vein_big");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL = create("ore_coal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_COAL_BURIED = create("ore_coal_buried");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON = create("ore_iron");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_IRON_SMALL = create("ore_iron_small");

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VIBRANIUM_SMALL = create("ore_vibranium_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VIBRANIUM_MEDIUM = create("ore_vibranium_medium");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VIBRANIUM_LARGE = create("ore_vibranium_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_VIBRANIUM_BURIED = create("ore_vibranium_buried");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest meteoriteTest = new BlockMatchTest(PalladiumBlocks.METEORITE_STONE.get());
        List<OreConfiguration.TargetBlockState> blackstoneReplace = List.of(OreConfiguration.target(meteoriteTest, Blocks.BLACKSTONE.defaultBlockState()));
        List<OreConfiguration.TargetBlockState> coalReplace = List.of(OreConfiguration.target(meteoriteTest, PalladiumBlocks.METEORITE_COAL_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> ironReplace = List.of(OreConfiguration.target(meteoriteTest, PalladiumBlocks.METEORITE_IRON_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> vibraniumReplace = List.of(OreConfiguration.target(meteoriteTest, PalladiumBlocks.METEORITE_VIBRANIUM_ORE.get().defaultBlockState()));
        List<OreConfiguration.TargetBlockState> vibraniumVeinReplace = List.of(OreConfiguration.target(meteoriteTest, PalladiumBlocks.METEORITE_VIBRANIUM_VEIN.get().defaultBlockState()));

        FeatureUtils.register(context, METEORITE_STONE_PILLAR, PalladiumFeatures.METEORITE_STONE_PILLAR.get());
        FeatureUtils.register(context, ORE_BLACKSTONE, Feature.ORE, new OreConfiguration(blackstoneReplace, 64));
        FeatureUtils.register(context, ORE_VIBRANIUM_VEIN_SMALL, Feature.ORE, new OreConfiguration(vibraniumVeinReplace, 6));
        FeatureUtils.register(context, ORE_VIBRANIUM_VEIN_BIG, Feature.ORE, new OreConfiguration(vibraniumVeinReplace, 46));

        FeatureUtils.register(context, ORE_COAL, Feature.ORE, new OreConfiguration(coalReplace, 17));
        FeatureUtils.register(context, ORE_COAL_BURIED, Feature.ORE, new OreConfiguration(coalReplace, 17, 0.5F));

        FeatureUtils.register(context, ORE_IRON, Feature.ORE, new OreConfiguration(ironReplace, 9));
        FeatureUtils.register(context, ORE_IRON_SMALL, Feature.ORE, new OreConfiguration(ironReplace, 4));

        FeatureUtils.register(context, ORE_VIBRANIUM_SMALL, Feature.ORE, new OreConfiguration(vibraniumReplace, 4, 0.5F));
        FeatureUtils.register(context, ORE_VIBRANIUM_LARGE, Feature.ORE, new OreConfiguration(vibraniumReplace, 12, 0.7F));
        FeatureUtils.register(context, ORE_VIBRANIUM_BURIED, Feature.ORE, new OreConfiguration(vibraniumReplace, 8, 1.0F));
        FeatureUtils.register(context, ORE_VIBRANIUM_MEDIUM, Feature.ORE, new OreConfiguration(vibraniumReplace, 8, 0.5F));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Palladium.id(name));
    }

}
