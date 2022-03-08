package net.threetag.palladium.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.PalladiumBlockUtil;

public class PalladiumBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Palladium.MOD_ID, Registry.BLOCK_REGISTRY);

    public static final RegistrySupplier<Block> LEAD_ORE = BLOCKS.register("lead_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistrySupplier<Block> DEEPSLATE_LEAD_ORE = BLOCKS.register("deepslate_lead_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(LEAD_ORE.get()).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistrySupplier<Block> SILVER_ORE = BLOCKS.register("silver_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F)));
    public static final RegistrySupplier<Block> DEEPSLATE_SILVER_ORE = BLOCKS.register("deepslate_silver_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(SILVER_ORE.get()).color(MaterialColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE)));
    public static final RegistrySupplier<Block> TITANIUM_ORE = BLOCKS.register("titanium_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).requiresCorrectToolForDrops()));
    public static final RegistrySupplier<Block> VIBRANIUM_ORE = BLOCKS.register("vibranium_ore", () -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.BLACKSTONE).requiresCorrectToolForDrops().lightLevel(value -> 4)));

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
