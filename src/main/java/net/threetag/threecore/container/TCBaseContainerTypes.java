package net.threetag.threecore.container;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ObjectHolder;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.block.ConstructionTableBlock;
import net.threetag.threecore.gui.screen.inventory.*;
import net.threetag.threecore.tileentity.FluidComposerTileEntity;
import net.threetag.threecore.tileentity.StirlingGeneratorTileEntity;
import net.threetag.threecore.util.icon.ItemIcon;

@ObjectHolder(ThreeCore.MODID)
public class TCBaseContainerTypes {

    @ObjectHolder("helmet_crafting")
    public static final ContainerType<HelmetCraftingContainer> HELMET_CRAFTING = null;

    @ObjectHolder("chestplate_crafting")
    public static final ContainerType<ChestplateCraftingContainer> CHESTPLATE_CRAFTING = null;

    @ObjectHolder("leggings_crafting")
    public static final ContainerType<LeggingsCraftingContainer> LEGGINGS_CRAFTING = null;

    @ObjectHolder("boots_crafting")
    public static final ContainerType<BootsCraftingContainer> BOOTS_CRAFTING = null;

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
        e.getRegistry().register(new ContainerType<>(HelmetCraftingContainer::new).setRegistryName(ThreeCore.MODID, "helmet_crafting"));
        e.getRegistry().register(new ContainerType<>(ChestplateCraftingContainer::new).setRegistryName(ThreeCore.MODID, "chestplate_crafting"));
        e.getRegistry().register(new ContainerType<>(LeggingsCraftingContainer::new).setRegistryName(ThreeCore.MODID, "leggings_crafting"));
        e.getRegistry().register(new ContainerType<>(BootsCraftingContainer::new).setRegistryName(ThreeCore.MODID, "boots_crafting"));
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

        // Construction Table Tabs
        ConstructionTableBlock.registerTab(new ResourceLocation(ThreeCore.MODID, "helmet_crafting"), new ConstructionTableBlock.Tab(() -> HELMET_CRAFTING, (id, playerInventory, player, world, pos) -> new HelmetCraftingContainer(id, playerInventory, IWorldPosCallable.of(world, pos)), new ItemIcon(Items.IRON_HELMET)));
        ConstructionTableBlock.registerTab(new ResourceLocation(ThreeCore.MODID, "chestplate_crafting"), new ConstructionTableBlock.Tab(() -> CHESTPLATE_CRAFTING, (id, playerInventory, player, world, pos) -> new ChestplateCraftingContainer(id, playerInventory, IWorldPosCallable.of(world, pos)), new ItemIcon(Items.IRON_CHESTPLATE)));
        ConstructionTableBlock.registerTab(new ResourceLocation(ThreeCore.MODID, "leggings_crafting"), new ConstructionTableBlock.Tab(() -> LEGGINGS_CRAFTING, (id, playerInventory, player, world, pos) -> new LeggingsCraftingContainer(id, playerInventory, IWorldPosCallable.of(world, pos)), new ItemIcon(Items.IRON_LEGGINGS)));
        ConstructionTableBlock.registerTab(new ResourceLocation(ThreeCore.MODID, "boots_crafting"), new ConstructionTableBlock.Tab(() -> BOOTS_CRAFTING, (id, playerInventory, player, world, pos) -> new BootsCraftingContainer(id, playerInventory, IWorldPosCallable.of(world, pos)), new ItemIcon(Items.IRON_BOOTS)));
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            // Screens
            ScreenManager.registerFactory(HELMET_CRAFTING, ConstructionTableScreen.Helmet::new);
            ScreenManager.registerFactory(CHESTPLATE_CRAFTING, ConstructionTableScreen.Chestplate::new);
            ScreenManager.registerFactory(LEGGINGS_CRAFTING, ConstructionTableScreen.Leggings::new);
            ScreenManager.registerFactory(BOOTS_CRAFTING, ConstructionTableScreen.Boots::new);
            ScreenManager.registerFactory(GRINDER, GrinderScreen::new);
            ScreenManager.registerFactory(HYDRAULIC_PRESS, HydraulicPressScreen::new);
            ScreenManager.registerFactory(FLUID_COMPOSER, FluidComposerScreen::new);
            ScreenManager.registerFactory(STIRLING_GENERATOR, StirlingGeneratorScreen::new);
            ScreenManager.registerFactory(CAPACITOR_BLOCK, CapacitorBlockScreen::new);
        });
    }

}
