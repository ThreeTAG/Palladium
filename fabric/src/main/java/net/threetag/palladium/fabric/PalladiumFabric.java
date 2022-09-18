package net.threetag.palladium.fabric;

import net.fabricmc.api.ModInitializer;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.block.entity.PalladiumBlockEntityTypes;
import net.threetag.palladium.compat.trinkets.fabric.TrinketsCompat;
import net.threetag.palladium.energy.IBlockEntityEnergyContainer;
import net.threetag.palladiumcore.util.Platform;
import team.reborn.energy.api.EnergyStorage;

public class PalladiumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Palladium.init();
        ModLoadingContext.registerConfig(Palladium.MOD_ID, ModConfig.Type.CLIENT, PalladiumConfig.Client.generateConfig());

        if (Platform.isModLoaded("trinkets")) {
            TrinketsCompat.init();
        }

        registerEnergyHandlers();
    }

    private static void registerEnergyHandlers() {
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> (EnergyStorage) ((IBlockEntityEnergyContainer) blockEntity).getEnergyStorage(direction), PalladiumBlockEntityTypes.SOLAR_PANEL.get());
    }

}
