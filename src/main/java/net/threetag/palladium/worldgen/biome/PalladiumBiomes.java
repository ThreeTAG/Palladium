package net.threetag.palladium.worldgen.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.AmbientParticle;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.worldgen.feature.PalladiumPlacedFeatures;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public class PalladiumBiomes {

    public static final ResourceKey<Biome> VIBRANIUM_METEORITE = create("vibranium_meteorite");

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> configuredFeatures = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(VIBRANIUM_METEORITE, vibraniumMeteorite(placedFeatures, configuredFeatures));
    }

    public static void registerTerrablenderRegions() {
        Regions.register(new MeteoriteRegion(Palladium.id("vibranium_meteorite"), 1));
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, Palladium.MOD_ID, createMeteoriteBiomeRules());
    }

    public static Biome vibraniumMeteorite(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobspawnsettings$builder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(mobspawnsettings$builder);

        BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(genSettings);
        BiomeDefaultFeatures.addDefaultCrystalFormations(genSettings);
        BiomeDefaultFeatures.addDefaultMonsterRoom(genSettings);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(genSettings);
        BiomeDefaultFeatures.addDefaultSprings(genSettings);
        BiomeDefaultFeatures.addSurfaceFreezing(genSettings);
        BiomeDefaultFeatures.addPlainGrass(genSettings);
        BiomeDefaultFeatures.addDefaultOres(genSettings);
        BiomeDefaultFeatures.addDefaultSoftDisks(genSettings);

        genSettings.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, PalladiumPlacedFeatures.METEORITE_STONE_PILLAR);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_BLACKSTONE);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_VIBRANIUM_VEIN_SMALL);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_VIBRANIUM_VEIN_BIG);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_COAL_UPPER);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_COAL_LOWER);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_IRON_UPPER);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_IRON_MIDDLE);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_IRON_SMALL);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_VIBRANIUM);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_VIBRANIUM_MEDIUM);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_VIBRANIUM_LARGE);
        genSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PalladiumPlacedFeatures.ORE_VIBRANIUM_BURIED);

        return baseBiome(0.5F, 0.5F)
                .setAttribute(EnvironmentAttributes.FOG_COLOR, ARGB.color(70, 140, 250))
                .setAttribute(EnvironmentAttributes.FOG_END_DISTANCE, 100F)
                .setAttribute(EnvironmentAttributes.AMBIENT_PARTICLES, AmbientParticle.of(ParticleTypes.GLOW, 0.001F))
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(0x67b0f6).foliageColorOverride(0x67b0f6).grassColorOverride(0x67b0f6).build())
                .mobSpawnSettings(mobspawnsettings$builder.build())
                .generationSettings(genSettings.build())
                .build();
    }

    private static Biome.BiomeBuilder baseBiome(float temperature, float downfall) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .temperature(temperature)
                .downfall(downfall)
                .setAttribute(EnvironmentAttributes.SKY_COLOR, OverworldBiomes.calculateSkyColor(temperature))
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(0x67b0f6).build());
    }

    public static SurfaceRules.RuleSource createMeteoriteBiomeRules() {
        SurfaceRules.RuleSource meteoriteStone = SurfaceRules.state(PalladiumBlocks.METEORITE_STONE.get().defaultBlockState());
        return SurfaceRules.ifTrue(SurfaceRules.isBiome(VIBRANIUM_METEORITE), SurfaceRules.sequence(
                bedrock(),
                meteoriteStone
        ));
    }

    private static SurfaceRules.RuleSource bedrock() {
        SurfaceRules.RuleSource bedrock = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState());
        SurfaceRules.ConditionSource bedrockCondition = SurfaceRules.verticalGradient("bedrock", VerticalAnchor.bottom(), VerticalAnchor.aboveBottom(5));
        return SurfaceRules.ifTrue(bedrockCondition, bedrock);
    }

    private static ResourceKey<Biome> create(String key) {
        return ResourceKey.create(Registries.BIOME, Palladium.id(key));
    }

}
