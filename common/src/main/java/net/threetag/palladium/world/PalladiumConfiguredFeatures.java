package net.threetag.palladium.world;

import com.google.common.base.Suppliers;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

import java.util.List;
import java.util.function.Supplier;

public class PalladiumConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Palladium.MOD_ID, Registry.CONFIGURED_FEATURE_REGISTRY);

    public static final Supplier<List<OreConfiguration.TargetBlockState>> LEAD_ORE_REPLACEMENTS = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, PalladiumBlocks.LEAD_ORE.get().defaultBlockState()),
            OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, PalladiumBlocks.DEEPSLATE_LEAD_ORE.get().defaultBlockState())
    ));
    public static final Supplier<List<OreConfiguration.TargetBlockState>> METEORITE_REPLACEMENTS = Suppliers.memoize(() -> List.of(
            OreConfiguration.target(OreFeatures.NATURAL_STONE, Blocks.BLACKSTONE.defaultBlockState())
    ));

    public static final RegistrySupplier<ConfiguredFeature<?, ?>> ORE_LEAD = CONFIGURED_FEATURES.register("ore_lead",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(LEAD_ORE_REPLACEMENTS.get(), 9)));

    public static final RegistrySupplier<ConfiguredFeature<?, ?>> UNDERGROUND_VIBRANIUM_METEORITE = CONFIGURED_FEATURES.register("underground_vibranium_meteorite",
            () -> new ConfiguredFeature<>(PalladiumFeatures.UNDERGROUND_VIBRANIUM_METEORITE.get(), new OreConfiguration(METEORITE_REPLACEMENTS.get(), 3)));

}
