package net.threetag.palladium.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.SpecialGuiElementRegistry;
import net.minecraft.client.renderer.RenderPipelines;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.client.PalladiumClient;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderer;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.core.registry.SimpleRegister;

public final class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AddonPackManager.initiateBasicLoaders();
        AddonPackManager.initiateAllLoaders(SimpleRegister::register);
        PalladiumClient.init();

        SpecialGuiElementRegistry.register(ctx -> new GuiMultiEntityRenderer(ctx.vertexConsumers(), ctx.client().getEntityRenderDispatcher()));
        RenderPipelines.register(PalladiumRenderTypes.Pipelines.ADD);
    }

}
