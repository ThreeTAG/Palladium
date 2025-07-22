package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayer;
import net.threetag.palladium.customization.BuiltinCustomization;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class RenderLayerProvider extends FabricCodecDataProvider<PackRenderLayer<?>> {

    public RenderLayerProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "palladium/render_layers", PackRenderLayer.Codecs.SIMPLE_CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, PackRenderLayer<?>> provider, HolderLookup.Provider lookup) {
        for (BuiltinCustomization.Type type : BuiltinCustomization.Type.values()) {
            provider.accept(type.getRenderLayerId(), type.makeRenderLayer());
        }
    }

    @Override
    public String getName() {
        return "Render Layers";
    }
}
