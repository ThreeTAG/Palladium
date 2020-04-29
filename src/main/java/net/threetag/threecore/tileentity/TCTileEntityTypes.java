package net.threetag.threecore.tileentity;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.block.CapacitorBlock;
import net.threetag.threecore.block.EnergyConduitBlock;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.client.renderer.tileentity.FluidComposerTileEntityRenderer;
import net.threetag.threecore.client.renderer.tileentity.HydraulicPressTileEntityRenderer;
import net.threetag.threecore.client.renderer.tileentity.StirlingGeneratorTileEntityRenderer;

public class TCTileEntityTypes {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, ThreeCore.MODID);

    public static final RegistryObject<TileEntityType<GrinderTileEntity>> GRINDER = TILE_ENTITIES.register("grinder", () -> TileEntityType.Builder.create(GrinderTileEntity::new, TCBlocks.GRINDER.get()).build(null));
    public static final RegistryObject<TileEntityType<HydraulicPressTileEntity>> HYDRAULIC_PRESS = TILE_ENTITIES.register("hydraulic_press", () -> TileEntityType.Builder.create(HydraulicPressTileEntity::new, TCBlocks.HYDRAULIC_PRESS.get()).build(null));
    public static final RegistryObject<TileEntityType<FluidComposerTileEntity>> FLUID_COMPOSER = TILE_ENTITIES.register("fluid_composer", () -> TileEntityType.Builder.create(FluidComposerTileEntity::new, TCBlocks.FLUID_COMPOSER.get()).build(null));
    public static final RegistryObject<TileEntityType<CapacitorBlockTileEntity>> CAPACITOR_BLOCK = TILE_ENTITIES.register("capacitor_block", () -> TileEntityType.Builder.create(() -> new CapacitorBlockTileEntity(CapacitorBlock.Type.NORMAL), TCBlocks.CAPACITOR_BLOCK.get(), TCBlocks.ADVANCED_CAPACITOR_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<StirlingGeneratorTileEntity>> STIRLING_GENERATOR = TILE_ENTITIES.register("stirling_generator", () -> TileEntityType.Builder.create(StirlingGeneratorTileEntity::new, TCBlocks.STIRLING_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<SolarPanelTileEntity>> SOLAR_PANEL = TILE_ENTITIES.register("solar_panel", () -> TileEntityType.Builder.create(SolarPanelTileEntity::new, TCBlocks.SOLAR_PANEL.get()).build(null));
    public static final RegistryObject<TileEntityType<EnergyConduitTileEntity>> CONDUIT = TILE_ENTITIES.register("conduit", () -> TileEntityType.Builder.create(() -> new EnergyConduitTileEntity(EnergyConduitBlock.ConduitType.GOLD), TCBlocks.GOLD_CONDUIT.get(), TCBlocks.COPPER_CONDUIT.get(), TCBlocks.SILVER_CONDUIT.get()).build(null));

    @OnlyIn(Dist.CLIENT)
    public static void initRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(HydraulicPressTileEntity.class, new HydraulicPressTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FluidComposerTileEntity.class, new FluidComposerTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(StirlingGeneratorTileEntity.class, new StirlingGeneratorTileEntityRenderer());
    }

}
