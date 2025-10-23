package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.concurrent.CompletableFuture;

public abstract class CustomizationProvider extends JsonCodecProvider<Customization> {

    public CustomizationProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, PackOutput.Target.DATA_PACK, PalladiumRegistryKeys.getPackFolder(PalladiumRegistryKeys.CUSTOMIZATION), Customization.Codecs.SIMPLE_CODEC, lookupProvider, modId);
    }
}
