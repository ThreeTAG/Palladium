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
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.attachment.neoforge.PlatformAttachmentTypeImpl;
import net.threetag.palladium.client.PalladiumClient;
import net.threetag.palladium.client.model.ModelLayerManager;

import java.util.Optional;
import java.util.function.Consumer;

@Mod(Palladium.MOD_ID)
@EventBusSubscriber(modid = Palladium.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
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

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client e) {
        // TODO
//        Palladium.generateDocumentation();
        var output = e.getGenerator().getPackOutput();

//        PalladiumBlockTagsProvider blockTagsProvider = new PalladiumBlockTagsProvider(output, e.getLookupProvider(), e.getExistingFileHelper());
//        e.getGenerator().addProvider(e.includeServer(), blockTagsProvider);
//        e.getGenerator().addProvider(e.includeServer(), new PalladiumItemTagsProvider(output, e.getLookupProvider(), e.getExistingFileHelper()));
//        e.getGenerator().addProvider(e.includeServer(), new PalladiumRecipeProvider(output, e.getLookupProvider()));
//        e.getGenerator().addProvider(e.includeServer(), new PalladiumLootTableProvider(output, e.getLookupProvider()));
//        e.getGenerator().addProvider(e.includeServer(), new PalladiumWorldGenProvider(output, e.getLookupProvider()));

//        e.getGenerator().addProvider(e.includeClient(), new PalladiumBlockStateProvider(output, e.getExistingFileHelper()));
//        e.getGenerator().addProvider(e.includeClient(), new PalladiumItemModelProvider(output, e.getExistingFileHelper()));
//        e.getGenerator().addProvider(e.includeClient(), new PalladiumSoundDefinitionsProvider(output, e.getExistingFileHelper()));
//        e.getGenerator().addProvider(true, new PalladiumLangProvider.English(output));
//        e.getGenerator().addProvider(true, new PalladiumLangProvider.German(output));
//        e.getGenerator().addProvider(true, new PalladiumLangProvider.Saxon(output));
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
