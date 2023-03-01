package net.threetag.palladium.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.addonpack.forge.AddonPackType;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.compat.curios.forge.CuriosCompat;
import net.threetag.palladium.compat.geckolib.forge.GeckoLibCompatImpl;
import net.threetag.palladium.data.forge.*;
import net.threetag.palladium.mixin.ReloadableResourceManagerMixin;
import net.threetag.palladiumcore.forge.PalladiumCoreForge;
import net.threetag.palladiumcore.util.Platform;

import java.util.List;

@Mod(Palladium.MOD_ID)
@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PalladiumForge {

    public PalladiumForge() {
        PalladiumCoreForge.registerModEventBus(Palladium.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        if (Platform.isModLoaded("curios")) {
            PalladiumCoreForge.registerModEventBus("curios", FMLJavaModLoadingContext.get().getModEventBus());
        }

        AddonPackType.init();
        Palladium.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PalladiumConfig.Client.generateConfig());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PalladiumConfig.Server.generateConfig());

        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.init();
        }

        if (Platform.isClient()) {
            PalladiumClient.init();

            if (ModList.get().isLoaded("curios")) {
                CuriosCompat.initClient();
            }

            if (Platform.isModLoaded("geckolib3")) {
                GeckoLibCompatImpl.init();
            }
        }
    }

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent e) {
        AddonPackManager.waitForLoading();
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent e) {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(PalladiumBlocks.HEART_SHAPED_HERB.getId(), PalladiumBlocks.POTTED_HEART_SHAPED_HERB);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent e) {
        Palladium.generateDocumentation();

        PalladiumBlockTagsProvider blockTagsProvider = new PalladiumBlockTagsProvider(e.getGenerator(), e.getExistingFileHelper());
        e.getGenerator().addProvider(e.includeServer(), blockTagsProvider);
        e.getGenerator().addProvider(e.includeServer(), new PalladiumItemTagsProvider(e.getGenerator(), blockTagsProvider, e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeServer(), new PalladiumRecipeProvider(e.getGenerator()));
        e.getGenerator().addProvider(e.includeServer(), new PalladiumLootTableProvider(e.getGenerator()));

        e.getGenerator().addProvider(e.includeClient(), new PalladiumBlockStateProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumItemModelProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumSoundDefinitionsProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.English(e.getGenerator()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.German(e.getGenerator()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.Saxon(e.getGenerator()));
    }

    @SubscribeEvent
    public static void packFinder(AddPackFindersEvent e) {
        e.addRepositorySource(AddonPackManager.getInstance().getWrappedPackFinder());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        Minecraft mc = Minecraft.getInstance();
        List<PreparableReloadListener> listeners = ((ReloadableResourceManagerMixin) mc.getResourceManager()).getListeners();
        int idx = listeners.indexOf(mc.getEntityModels());
        listeners.add(idx + 1, new ModelLayerManager());
    }

}
