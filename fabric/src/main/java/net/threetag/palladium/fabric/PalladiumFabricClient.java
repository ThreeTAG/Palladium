package net.threetag.palladium.fabric;

import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.server.packs.PackType;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.client.model.ArmorModelManager;
import net.threetag.palladium.client.renderer.entity.EffectEntityRenderer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.entity.PalladiumEntityTypes;

public class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PalladiumClient.init();
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new PackRenderLayerManager());
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new ArmorModelManager());
        EntityRendererRegistry.register(PalladiumEntityTypes.EFFECT.get(), EffectEntityRenderer::new);
    }

}
