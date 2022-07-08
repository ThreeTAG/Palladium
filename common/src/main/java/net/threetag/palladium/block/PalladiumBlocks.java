package net.threetag.palladium.block;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.DeferredRegistry;
import net.threetag.palladium.registry.RegistrySupplier;
import net.threetag.palladium.util.PalladiumBlockUtil;

public class PalladiumBlocks {

    public static final DeferredRegistry<Block> BLOCKS = DeferredRegistry.create(Palladium.MOD_ID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> LEAD_ORE = BLOCKS.register("lead_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistrySupplier<Block> DEEPSLATE_LEAD_ORE = BLOCKS.register("deepslate_lead_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(LEAD_ORE.get()).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistrySupplier<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistrySupplier<Block> DEEPSLATE_SILVER_ORE = BLOCKS.register("deepslate_silver_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(SILVER_ORE.get()).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistrySupplier<Block> TITANIUM_ORE = BLOCKS.register("titanium_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VIBRANIUM_ORE = BLOCKS.register("vibranium_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).requiresCorrectToolForDrops().lightLevel(value -> 4)));

    public static final RegistrySupplier<Block> REDSTONE_FLUX_CRYSTAL_GEODE = BLOCKS.register("redstone_flux_crystal_geode", () -> new FluxCrystalGeodeBlock(BlockBehaviour.Properties.copy(Blocks.REDSTONE_ORE).lightLevel(value -> 9)));
    public static final RegistrySupplier<Block> DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE = BLOCKS.register("deepslate_redstone_flux_crystal_geode", () -> new FluxCrystalGeodeBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_REDSTONE_ORE).lightLevel(value -> 9)));
    public static final RegistrySupplier<Block> REDSTONE_FLUX_CRYSTAL_CLUSTER = BLOCKS.register("redstone_flux_crystal_cluster", () -> new RedstoneFluxCrystalClusterBlock(7, 3, BlockBehaviour.Properties.copy(Blocks.AMETHYST_CLUSTER).color(MaterialColor.COLOR_RED)));
    public static final RegistrySupplier<Block> LARGE_REDSTONE_FLUX_CRYSTAL_BUD = BLOCKS.register("large_redstone_flux_crystal_bud", () -> new RedstoneFluxCrystalClusterBlock(5, 3, BlockBehaviour.Properties.copy(Blocks.LARGE_AMETHYST_BUD).color(MaterialColor.COLOR_RED)));
    public static final RegistrySupplier<Block> MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD = BLOCKS.register("medium_redstone_flux_crystal_bud", () -> new RedstoneFluxCrystalClusterBlock(4, 3, BlockBehaviour.Properties.copy(Blocks.MEDIUM_AMETHYST_BUD).color(MaterialColor.COLOR_RED)));
    public static final RegistrySupplier<Block> SMALL_REDSTONE_FLUX_CRYSTAL_BUD = BLOCKS.register("small_redstone_flux_crystal_bud", () -> new RedstoneFluxCrystalClusterBlock(3, 4, BlockBehaviour.Properties.copy(Blocks.SMALL_AMETHYST_BUD).color(MaterialColor.COLOR_RED)));

    public static final RegistrySupplier<Block> LEAD_BLOCK = BLOCKS.register("lead_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(4.0F, 12.0F)));
    public static final RegistrySupplier<Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));
    public static final RegistrySupplier<Block> TITANIUM_BLOCK = BLOCKS.register("titanium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(10.0F, 12.0F)));
    public static final RegistrySupplier<Block> VIBRANIUM_BLOCK = BLOCKS.register("vibranium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(15.0F, 18.0F)));

    public static final RegistrySupplier<Block> RAW_LEAD_BLOCK = BLOCKS.register("raw_lead_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));
    public static final RegistrySupplier<Block> RAW_SILVER_BLOCK = BLOCKS.register("raw_silver_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.METAL).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));
    public static final RegistrySupplier<Block> RAW_TITANIUM_BLOCK = BLOCKS.register("raw_titanium_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));
    public static final RegistrySupplier<Block> RAW_VIBRANIUM_BLOCK = BLOCKS.register("raw_vibranium_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_LIGHT_GRAY).requiresCorrectToolForDrops().sound(SoundType.METAL).strength(5.0F, 6.0F)));

    public static final RegistrySupplier<Block> SOLAR_PANEL = BLOCKS.register("solar_panel", () -> new SolarPanelBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F)));

    public static final RegistrySupplier<Block> HEART_SHAPED_HERB = BLOCKS.register("heart_shaped_herb", () -> new FlowerBlock(MobEffects.DAMAGE_RESISTANCE, 4, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> POTTED_HEART_SHAPED_HERB = BLOCKS.register("potted_heart_shaped_herb", () -> PalladiumBlockUtil.createFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, HEART_SHAPED_HERB, BlockBehaviour.Properties.of(Material.DECORATION).instabreak().noOcclusion()));

}
