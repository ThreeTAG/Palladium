package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.concurrent.CompletableFuture;

public abstract class PowerProvider extends JsonCodecProvider<Power> {

    public PowerProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, PackOutput.Target.DATA_PACK, PalladiumRegistryKeys.getPackFolder(PalladiumRegistryKeys.POWER), Power.CODEC, lookupProvider, modId);
    }
}
