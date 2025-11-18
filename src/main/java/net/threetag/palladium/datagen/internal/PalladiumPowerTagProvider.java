package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.datagen.PowerTagsProvider;
import net.threetag.palladium.tag.PalladiumPowerTags;

import java.util.concurrent.CompletableFuture;

public class PalladiumPowerTagProvider extends PowerTagsProvider {

    public PalladiumPowerTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(PalladiumPowerTags.IS_MECHANICAL);
        this.tag(PalladiumPowerTags.IS_MAGICAL);
        this.tag(PalladiumPowerTags.IS_GENETIC);
    }
}
