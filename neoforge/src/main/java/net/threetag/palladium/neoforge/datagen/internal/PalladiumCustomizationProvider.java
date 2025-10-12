package net.threetag.palladium.neoforge.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.BuiltinCustomization;
import net.threetag.palladium.neoforge.datagen.CustomizationProvider;

import java.util.concurrent.CompletableFuture;

public class PalladiumCustomizationProvider extends CustomizationProvider {

    public PalladiumCustomizationProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void gather() {
        for (BuiltinCustomization.Type type : BuiltinCustomization.Type.values()) {
            this.unconditional(Palladium.id(type.getSerializedName()), new BuiltinCustomization(type));
        }
    }
}
