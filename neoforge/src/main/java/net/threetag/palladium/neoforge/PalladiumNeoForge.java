package net.threetag.palladium.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterPictureInPictureRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.attachment.neoforge.PlatformAttachmentTypeImpl;
import net.threetag.palladium.client.PalladiumClient;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderState;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderer;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;

import java.util.Optional;
import java.util.function.Consumer;

@Mod(Palladium.MOD_ID)
@EventBusSubscriber(modid = Palladium.MOD_ID)
public final class PalladiumNeoForge {

    public PalladiumNeoForge() {
        Palladium.init();
        PlatformAttachmentTypeImpl.initEvents();

        if (Platform.getEnvironment() == Env.CLIENT) {
            PalladiumClient.init();
        }
    }

    @SubscribeEvent
    public static void packFinder(AddPackFindersEvent e) {
        if (e.getPackType() != AddonPackManager.getPackType()) {
            e.addRepositorySource(AddonPackManager.getWrappedPackFinder(e.getPackType()));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    @OnlyIn(Dist.CLIENT)
    public static void onRegisterClientReloadListener(AddClientReloadListenersEvent e) {
        var id = ModelLayerManager.ID;
        e.addListener(id, new ModelLayerManager());
        e.addDependency(e.getNameLookup().apply(Minecraft.getInstance().getModelManager()), id);
        e.addDependency(id, e.getNameLookup().apply(Minecraft.getInstance().getEntityRenderDispatcher().equipmentAssets));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegister(RegisterEvent e) {
        if (e.getRegistryKey() == Registries.ATTRIBUTE) {
            AddonPackManager.initiateBasicLoaders();
        }

        AddonPackManager.initiateFor(e.getRegistryKey(), (registry, id, object) -> e.register(registry, id, () -> object));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerPiP(RegisterPictureInPictureRenderersEvent e) {
        e.register(GuiMultiEntityRenderState.class, bufferSource -> new GuiMultiEntityRenderer(bufferSource, Minecraft.getInstance().getEntityRenderDispatcher()));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerPipelines(RegisterRenderPipelinesEvent e) {
        e.registerPipeline(PalladiumRenderTypes.Pipelines.ADD);
    }

    public static Optional<IEventBus> getModEventBus(String modId) {
        return ModList.get().getModContainerById(modId)
                .map(ModContainer::getEventBus);
    }

    public static void whenModBusAvailable(String modId, Consumer<IEventBus> busConsumer) {
        IEventBus bus = getModEventBus(modId).orElseThrow(() -> new IllegalStateException("Mod '" + modId + "' is not available!"));
        busConsumer.accept(bus);
    }
}
