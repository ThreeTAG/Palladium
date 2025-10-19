package net.threetag.palladium.neoforge;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderState;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderer;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.core.event.PalladiumLifecycleEvents;
import net.threetag.palladium.neoforge.datagen.internal.*;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class PalladiumNeoForgeClient {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client e) {
        // Client
        e.createProvider(PalladiumLangProvider.English::new);
        e.createProvider(PalladiumLangProvider.German::new);
        e.createProvider(PalladiumLangProvider.Saxon::new);
        e.createProvider(PalladiumItemModelProvider::new);
        e.createProvider(PalladiumRenderLayerProvider::new);
        e.createProvider(PalladiumBeamProvider::new);

        // Server
        e.createProvider(PalladiumDocumentationGenerator::new);
        e.createProvider(PalladiumBlockTagProvider::new);
        e.createProvider(PalladiumCustomizationProvider::new);
        e.createProvider(PalladiumCustomizationCategoryProvider::new);
        e.createProvider(PalladiumFlightTypeProvider::new);
        e.createProvider(PalladiumRecipeProvider.Runner::new);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void event(FMLClientSetupEvent event) {
        event.enqueueWork(() -> PalladiumLifecycleEvents.CLIENT_SETUP.invoker().run());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRegisterClientReloadListener(AddClientReloadListenersEvent e) {
        var id = ModelLayerManager.ID;
        e.addListener(id, new ModelLayerManager());
        e.addDependency(e.getNameLookup().apply(Minecraft.getInstance().getModelManager()), id);
        e.addDependency(id, e.getNameLookup().apply(Minecraft.getInstance().getEntityRenderDispatcher().equipmentAssets));
    }

    @SubscribeEvent
    public static void registerPiP(RegisterPictureInPictureRenderersEvent e) {
        e.register(GuiMultiEntityRenderState.class, bufferSource -> new GuiMultiEntityRenderer(bufferSource, Minecraft.getInstance().getEntityRenderDispatcher()));
    }

    @SubscribeEvent
    public static void registerPipelines(RegisterRenderPipelinesEvent e) {
        e.registerPipeline(PalladiumRenderTypes.Pipelines.ADD);
    }

}
