package net.threetag.palladium.forge;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.resource.ResourcePackLoader;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.client.model.ModelLayerManager;
import net.threetag.palladium.compat.curios.forge.CuriosCompat;
import net.threetag.palladium.compat.geckolib.forge.GeckoLibCompatImpl;
import net.threetag.palladium.data.forge.*;
import net.threetag.palladium.mixin.ReloadableResourceManagerMixin;
import net.threetag.palladiumcore.forge.PalladiumCoreForge;
import net.threetag.palladiumcore.util.Platform;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mod(Palladium.MOD_ID)
@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PalladiumForge {

    public PalladiumForge() {
        PalladiumCoreForge.registerModEventBus(Palladium.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        if (Platform.isModLoaded("curios")) {
            PalladiumCoreForge.registerModEventBus("curios", FMLJavaModLoadingContext.get().getModEventBus());
        }

        Palladium.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PalladiumConfig.Client.generateConfig());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, PalladiumConfig.Server.generateConfig());

        if (ModList.get().isLoaded("curios")) {
            CuriosCompat.init();
        }

        if (Platform.isModLoaded("geckolib")) {
            GeckoLibCompatImpl.init();
        }

        if (Platform.isClient()) {
            PalladiumClient.init();

            if (ModList.get().isLoaded("curios")) {
                CuriosCompat.initClient();
            }
        }
    }

    @SubscribeEvent
    public static void newRegistry(NewRegistryEvent e) {
        AddonPackManager.waitForLoading();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onConstructMod(FMLConstructModEvent event) {
        event.enqueueWork(() -> {
            var addonPacks = AddonPackManager.getInstance();
            ResourcePackLoader.loadResourcePacks(addonPacks.getPackList(), (mods) -> (infoConsumer) -> {
                for (Map.Entry<IModFile, ? extends PathPackResources> e : mods.entrySet()) {
                    IModInfo mod = e.getKey().getModInfos().get(0);
                    if (Objects.equals(mod.getModId(), "minecraft")) continue;
                    final String name = "mod:" + mod.getModId();
                    final Pack packInfo = Pack.readMetaAndCreate(name, Component.literal(e.getValue().packId()), false, id -> e.getValue(), AddonPackManager.getPackType(), Pack.Position.BOTTOM, PackSource.DEFAULT);
                    if (packInfo == null) {
                        ModLoader.get().addWarning(new ModLoadingWarning(mod, ModLoadingStage.ERROR, "fml.modloading.brokenresources", e.getKey()));
                        continue;
                    }
                    infoConsumer.accept(packInfo);
                }
            });

            AddonPackManager.startLoading();
        });
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent e) {
        ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(PalladiumBlocks.HEART_SHAPED_HERB.getId(), PalladiumBlocks.POTTED_HEART_SHAPED_HERB);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent e) {
        Palladium.generateDocumentation();
        var output = e.getGenerator().getPackOutput();

        PalladiumBlockTagsProvider blockTagsProvider = new PalladiumBlockTagsProvider(output, e.getLookupProvider(), e.getExistingFileHelper());
        e.getGenerator().addProvider(e.includeServer(), blockTagsProvider);
        e.getGenerator().addProvider(e.includeServer(), new PalladiumItemTagsProvider(output, e.getLookupProvider(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeServer(), new PalladiumRecipeProvider(output));
        e.getGenerator().addProvider(e.includeServer(), new PalladiumLootTableProvider(output));

        e.getGenerator().addProvider(e.includeClient(), new PalladiumBlockStateProvider(output, e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumItemModelProvider(output, e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumSoundDefinitionsProvider(output, e.getExistingFileHelper()));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.English(output));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.German(output));
        e.getGenerator().addProvider(e.includeClient(), new PalladiumLangProvider.Saxon(output));
    }

    @SubscribeEvent
    public static void packFinder(AddPackFindersEvent e) {
        if (e.getPackType() != AddonPackManager.getPackType()) {
            e.addRepositorySource(AddonPackManager.getInstance().getWrappedPackFinder());
        }
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
