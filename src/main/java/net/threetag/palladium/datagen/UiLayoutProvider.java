package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.client.gui.ui.layout.UiLayout;

import java.util.concurrent.CompletableFuture;

public abstract class UiLayoutProvider extends AdvancedJsonCodecProvider<UiLayout> {

    public UiLayoutProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, PackOutput.Target.RESOURCE_PACK, "palladium/screens", UiLayout.Codecs.CODEC, lookupProvider, modId);
    }
}
