package net.threetag.palladium.world;

import com.google.common.base.Suppliers;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;

import java.util.List;
import java.util.function.Supplier;

public class PalladiumConfiguredFeatures {

    public static final Supplier<List<OreConfiguration.TargetBlockState>> LEAD_ORE_REPLACEMENTS = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), PalladiumBlocks.LEAD_ORE.get().defaultBlockState()),
            OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), PalladiumBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())
    ));
    public static final Supplier<List<OreConfiguration.TargetBlockState>> METEORITE_REPLACEMENTS = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), Blocks.BLACKSTONE.defaultBlockState())
    ));

    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_LEAD = registerKey("ore_lead");
    public static final ResourceKey<ConfiguredFeature<?, ?>> UNDERGROUND_VIBRANIUM_METEORITE = registerKey("underground_vibranium_meteorite");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        register(context, ORE_LEAD, Feature.ORE, new OreConfiguration(LEAD_ORE_REPLACEMENTS.get(), 9));
        register(context, UNDERGROUND_VIBRANIUM_METEORITE, PalladiumFeatures.UNDERGROUND_VIBRANIUM_METEORITE.get(), new OreConfiguration(METEORITE_REPLACEMENTS.get(), 3));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Palladium.id(name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
