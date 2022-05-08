package net.threetag.palladium.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.addonpack.forge.AddonPackType;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.ArmorModelManager;
import net.threetag.palladium.client.model.EntityModelManager;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.data.forge.*;
import net.threetag.palladium.mixin.ReloadableResourceManagerMixin;

import java.util.List;

@Mod(Palladium.MOD_ID)
@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PalladiumForge {

    public PalladiumForge() {
        AddonPackType.init();
        EventBuses.registerModEventBus(Palladium.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Palladium.init();
        PalladiumConfigImpl.init();
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent e) {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(PalladiumBlocks.HEART_SHAPED_HERB.getId(), PalladiumBlocks.POTTED_HEART_SHAPED_HERB);
    }

    @SubscribeEvent
    public static void setupClient(FMLClientSetupEvent e) {
        PalladiumClient.init();
    }

    @SubscribeEvent
    public static void reloadRegisterClient(RegisterClientReloadListenersEvent e) {
        e.registerReloadListener(new PackRenderLayerManager());
        e.registerReloadListener(new ArmorModelManager());
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent e) {
        Palladium.generateDocumentation();

        PalladiumBlockTagsProvider blockTagsProvider = new PalladiumBlockTagsProvider(e.getGenerator(), e.getExistingFileHelper());
        e.getGenerator().addProvider(blockTagsProvider);
        e.getGenerator().addProvider(new PalladiumItemTagsProvider(e.getGenerator(), blockTagsProvider, e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumRecipeProvider(e.getGenerator()));
        e.getGenerator().addProvider(new PalladiumLootTableProvider(e.getGenerator()));
        e.getGenerator().addProvider(new PalladiumBlockModelProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumBlockStateProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumItemModelProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumSoundDefinitionsProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumLangProvider.English(e.getGenerator()));
        e.getGenerator().addProvider(new PalladiumLangProvider.German(e.getGenerator()));
        e.getGenerator().addProvider(new PalladiumLangProvider.Saxon(e.getGenerator()));
    }

    @SubscribeEvent
    public static void packFinder(AddPackFindersEvent e) {
        e.addRepositorySource(AddonPackManager.getInstance().getWrappedPackFinder());
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        Minecraft mc = Minecraft.getInstance();
        List<PreparableReloadListener> listeners = ((ReloadableResourceManagerMixin) mc.getResourceManager()).getListeners();
        int idx = listeners.indexOf(mc.getEntityModels());
        listeners.add(idx + 1, new EntityModelManager());
    }

}
