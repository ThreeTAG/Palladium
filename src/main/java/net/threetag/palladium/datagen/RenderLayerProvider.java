package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;

import java.util.concurrent.CompletableFuture;

public abstract class RenderLayerProvider extends JsonCodecProvider<PackRenderLayer<?>> {

    public RenderLayerProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, PackOutput.Target.RESOURCE_PACK, "palladium/render_layers", PackRenderLayer.Codecs.SIMPLE_CODEC, lookupProvider, modId);
    }
}
