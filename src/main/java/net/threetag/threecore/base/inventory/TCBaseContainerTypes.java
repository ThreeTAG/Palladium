package net.threetag.threecore.base.inventory;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.client.gui.*;
import net.threetag.threecore.base.tileentity.FluidComposerTileEntity;
import net.threetag.threecore.base.tileentity.StirlingGeneratorTileEntity;

@ObjectHolder(ThreeCore.MODID)
public class TCBaseContainerTypes {

    @ObjectHolder("grinder")
    public static final ContainerType<GrinderContainer> GRINDER = null;

    @ObjectHolder("hydraulic_press")
    public static final ContainerType<HydraulicPressContainer> HYDRAULIC_PRESS = null;

    @ObjectHolder("fluid_composer")
    public static final ContainerType<FluidComposerContainer> FLUID_COMPOSER = null;

    @ObjectHolder("capacitor_block")
    public static final ContainerType<CapacitorBlockContainer> CAPACITOR_BLOCK = null;

    @ObjectHolder("stirling_generator")
    public static final ContainerType<StirlingGeneratorContainer> STIRLING_GENERATOR = null;

    @SubscribeEvent
    public void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> e) {
        e.getRegistry().register(new ContainerType<>(GrinderContainer::new).setRegistryName(ThreeCore.MODID, "grinder"));
        e.getRegistry().register(new ContainerType<>(HydraulicPressContainer::new).setRegistryName(ThreeCore.MODID, "hydraulic_press"));

        e.getRegistry().register(new ContainerType<>((IContainerFactory<Container>) (windowId, inv, data) -> {
            TileEntity tileEntity = inv.player.world.getTileEntity(data.readBlockPos());
            return tileEntity instanceof FluidComposerTileEntity ? new FluidComposerContainer(windowId, inv, (FluidComposerTileEntity) tileEntity) : null;
        }).setRegistryName(ThreeCore.MODID, "fluid_composer"));

        e.getRegistry().register(new ContainerType<>((IContainerFactory<Container>) (windowId, inv, data) -> {
            TileEntity tileEntity = inv.player.world.getTileEntity(data.readBlockPos());
            return tileEntity instanceof StirlingGeneratorTileEntity ? new StirlingGeneratorContainer(windowId, inv, (StirlingGeneratorTileEntity) tileEntity) : null;
        }).setRegistryName(ThreeCore.MODID, "stirling_generator"));

        e.getRegistry().register(new ContainerType<>(CapacitorBlockContainer::new).setRegistryName(ThreeCore.MODID, "capacitor_block"));
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            // Screens
            ScreenManager.registerFactory(GRINDER, GrinderScreen::new);
            ScreenManager.registerFactory(HYDRAULIC_PRESS, HydraulicPressScreen::new);
            ScreenManager.registerFactory(FLUID_COMPOSER, FluidComposerScreen::new);
            ScreenManager.registerFactory(STIRLING_GENERATOR, StirlingGeneratorScreen::new);
            ScreenManager.registerFactory(CAPACITOR_BLOCK, CapacitorBlockScreen::new);
        });
    }

}
