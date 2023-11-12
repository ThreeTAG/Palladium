package net.threetag.palladium.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.compat.geckolib.fabric.GeckoLibCompatImpl;
import net.threetag.palladium.compat.trinkets.fabric.TrinketsCompat;
import net.threetag.palladium.loot.LootTableModificationManager;
import net.threetag.palladium.world.PalladiumPlacedFeatures;
import net.threetag.palladiumcore.registry.RegistrySupplier;
import net.threetag.palladiumcore.util.Platform;

import java.util.function.Function;

public class PalladiumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Palladium.init();
        ModLoadingContext.registerConfig(Palladium.MOD_ID, ModConfig.Type.CLIENT, PalladiumConfig.Client.generateConfig());
        ModLoadingContext.registerConfig(Palladium.MOD_ID, ModConfig.Type.SERVER, PalladiumConfig.Server.generateConfig());

        if (Platform.isModLoaded("trinkets")) {
            TrinketsCompat.init();
        }

        if (Platform.isModLoaded("geckolib3")) {
            GeckoLibCompatImpl.init();
        }

        registerEnergyHandlers();
        registerEvents();
        registerPlacedFeatures();
    }

    private static void registerEnergyHandlers() {

    }

    private static void registerEvents() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            LootTableModificationManager.Modification mod = LootTableModificationManager.getInstance().getFor(id);

            if (mod != null) {
                for (LootPool lootPool : mod.getLootPools()) {
                    tableBuilder.pool(lootPool);
                }
            }
        });
    }

    private static void registerPlacedFeatures() {
        Function<RegistrySupplier<PlacedFeature>, ResourceKey<PlacedFeature>> converter = feature -> ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, feature.getId());
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, converter.apply(PalladiumPlacedFeatures.ORE_LEAD_UPPER));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, converter.apply(PalladiumPlacedFeatures.ORE_LEAD_MIDDLE));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, converter.apply(PalladiumPlacedFeatures.ORE_LEAD_SMALL));
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.VEGETAL_DECORATION, converter.apply(PalladiumPlacedFeatures.UNDERGROUND_VIBRANIUM_METEORITE));
    }

}
