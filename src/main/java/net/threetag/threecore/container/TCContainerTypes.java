package net.threetag.threecore.container;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.block.ConstructionTableBlock;
import net.threetag.threecore.client.gui.inventory.*;
import net.threetag.threecore.tileentity.StirlingGeneratorTileEntity;
import net.threetag.threecore.util.icon.ItemIcon;

public class TCContainerTypes {

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = new DeferredRegister<>(ForgeRegistries.CONTAINERS, ThreeCore.MODID);

    public static final RegistryObject<ContainerType<HelmetCraftingContainer>> HELMET_CRAFTING = CONTAINER_TYPES.register("helmet_crafting", () -> new ContainerType<>(HelmetCraftingContainer::new));

    public static final RegistryObject<ContainerType<ChestplateCraftingContainer>> CHESTPLATE_CRAFTING = CONTAINER_TYPES.register("chestplate_crafting", () -> new ContainerType<>(ChestplateCraftingContainer::new));

    public static final RegistryObject<ContainerType<LeggingsCraftingContainer>> LEGGINGS_CRAFTING = CONTAINER_TYPES.register("leggings_crafting", () -> new ContainerType<>(LeggingsCraftingContainer::new));

    public static final RegistryObject<ContainerType<BootsCraftingContainer>> BOOTS_CRAFTING = CONTAINER_TYPES.register("boots_crafting", () -> new ContainerType<>(BootsCraftingContainer::new));

    public static final RegistryObject<ContainerType<GrinderContainer>> GRINDER = CONTAINER_TYPES.register("grinder", () -> new ContainerType<>(GrinderContainer::new));

    public static final RegistryObject<ContainerType<HydraulicPressContainer>> HYDRAULIC_PRESS = CONTAINER_TYPES.register("hydraulic_press", () -> new ContainerType<>(HydraulicPressContainer::new));

    public static final RegistryObject<ContainerType<FluidComposerContainer>> FLUID_COMPOSER = CONTAINER_TYPES.register("fluid_composer", () -> new ContainerType<>(FluidComposerContainer::new));

    public static final RegistryObject<ContainerType<CapacitorBlockContainer>> CAPACITOR_BLOCK = CONTAINER_TYPES.register("capacitor_block", () -> new ContainerType<>(CapacitorBlockContainer::new));

    public static final RegistryObject<ContainerType<StirlingGeneratorContainer>> STIRLING_GENERATOR = CONTAINER_TYPES.register("stirling_generator", () -> new ContainerType<>((IContainerFactory<StirlingGeneratorContainer>) (windowId, inv, data) -> {
        TileEntity tileEntity = inv.player.world.getTileEntity(data.readBlockPos());
        return tileEntity instanceof StirlingGeneratorTileEntity ? new StirlingGeneratorContainer(windowId, inv, (StirlingGeneratorTileEntity) tileEntity) : null;
    }));

    public static void registerConstructionTableTabls() {
        ConstructionTableBlock.registerTab(new ResourceLocation(ThreeCore.MODID, "helmet_crafting"), new ConstructionTableBlock.Tab(HELMET_CRAFTING::get, (id, playerInventory, player, world, pos) -> new HelmetCraftingContainer(id, playerInventory, IWorldPosCallable.of(world, pos)), new ItemIcon(Items.IRON_HELMET)));
        ConstructionTableBlock.registerTab(new ResourceLocation(ThreeCore.MODID, "chestplate_crafting"), new ConstructionTableBlock.Tab(CHESTPLATE_CRAFTING::get, (id, playerInventory, player, world, pos) -> new ChestplateCraftingContainer(id, playerInventory, IWorldPosCallable.of(world, pos)), new ItemIcon(Items.IRON_CHESTPLATE)));
        ConstructionTableBlock.registerTab(new ResourceLocation(ThreeCore.MODID, "leggings_crafting"), new ConstructionTableBlock.Tab(LEGGINGS_CRAFTING::get, (id, playerInventory, player, world, pos) -> new LeggingsCraftingContainer(id, playerInventory, IWorldPosCallable.of(world, pos)), new ItemIcon(Items.IRON_LEGGINGS)));
        ConstructionTableBlock.registerTab(new ResourceLocation(ThreeCore.MODID, "boots_crafting"), new ConstructionTableBlock.Tab(BOOTS_CRAFTING::get, (id, playerInventory, player, world, pos) -> new BootsCraftingContainer(id, playerInventory, IWorldPosCallable.of(world, pos)), new ItemIcon(Items.IRON_BOOTS)));
    }

    @OnlyIn(Dist.CLIENT)
    public static void initContainerScreens() {
        ScreenManager.registerFactory(HELMET_CRAFTING.get(), ConstructionTableScreen.Helmet::new);
        ScreenManager.registerFactory(CHESTPLATE_CRAFTING.get(), ConstructionTableScreen.Chestplate::new);
        ScreenManager.registerFactory(LEGGINGS_CRAFTING.get(), ConstructionTableScreen.Leggings::new);
        ScreenManager.registerFactory(BOOTS_CRAFTING.get(), ConstructionTableScreen.Boots::new);
        ScreenManager.registerFactory(GRINDER.get(), GrinderScreen::new);
        ScreenManager.registerFactory(HYDRAULIC_PRESS.get(), HydraulicPressScreen::new);
        ScreenManager.registerFactory(FLUID_COMPOSER.get(), FluidComposerScreen::new);
        ScreenManager.registerFactory(STIRLING_GENERATOR.get(), StirlingGeneratorScreen::new);
        ScreenManager.registerFactory(CAPACITOR_BLOCK.get(), CapacitorBlockScreen::new);
    }

}
