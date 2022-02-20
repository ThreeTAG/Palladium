package net.threetag.palladium.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.forge.data.*;

@Mod(Palladium.MOD_ID)
@Mod.EventBusSubscriber(modid = Palladium.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PalladiumForge {

    public PalladiumForge() {
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
    public static void gatherData(GatherDataEvent e) {
        PalladiumBlockTagsProvider blockTagsProvider = new PalladiumBlockTagsProvider(e.getGenerator(), e.getExistingFileHelper());
        e.getGenerator().addProvider(blockTagsProvider);
        e.getGenerator().addProvider(new PalladiumItemTagsProvider(e.getGenerator(), blockTagsProvider, e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumRecipeProvider(e.getGenerator()));
        e.getGenerator().addProvider(new PalladiumLootTableProvider(e.getGenerator()));
        e.getGenerator().addProvider(new PalladiumBlockModelProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumBlockStateProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumItemModelProvider(e.getGenerator(), e.getExistingFileHelper()));
        e.getGenerator().addProvider(new PalladiumLangProvider.English(e.getGenerator()));
        e.getGenerator().addProvider(new PalladiumLangProvider.German(e.getGenerator()));
        e.getGenerator().addProvider(new PalladiumLangProvider.Saxon(e.getGenerator()));
    }

}
