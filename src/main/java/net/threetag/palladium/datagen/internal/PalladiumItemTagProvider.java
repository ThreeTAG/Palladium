package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tag.PalladiumItemTags;

import java.util.concurrent.CompletableFuture;

public class PalladiumItemTagProvider extends ItemTagsProvider {

    public PalladiumItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (DyeColor color : DyeColor.values()) {
            this.tag(PalladiumItemTags.FABRIC_BY_COLOR.get(color)).add(PalladiumItems.FABRIC_BY_COLOR.get(color).get());
            this.tag(PalladiumItemTags.FABRICS).addTag(PalladiumItemTags.FABRIC_BY_COLOR.get(color));
            this.tag(PalladiumItemTags.DYED_BY_COLOR.get(color)).add(PalladiumItems.FABRIC_BY_COLOR.get(color).get());
        }
    }
}
