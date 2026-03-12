package net.threetag.palladium.block;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.flag.PalladiumFeatureFlags;

import java.util.function.Supplier;

public class PalladiumBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Palladium.MOD_ID);

    // Utilities
    public static final DeferredBlock<Block> TAILORING_BENCH = BLOCKS.registerBlock("tailoring_bench", TailoringBenchBlock::new, p -> p.requiredFeatures(PalladiumFeatureFlags.TAILORING));

    // Resources
    public static final DeferredBlock<Block> METEORITE_STONE = BLOCKS.registerSimpleBlock("meteorite_stone", p -> p
            .mapColor(MapColor.COLOR_BLACK)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(4.0F, 8.0F)
            .sound(SoundType.DEEPSLATE)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredBlock<Block> METEORITE_STONE_STAIRS = registerStairs("meteorite_stone_stairs", METEORITE_STONE);
    public static final DeferredBlock<Block> METEORITE_STONE_WALL = BLOCKS.registerBlock("meteorite_stone_wall", WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(METEORITE_STONE.get()).forceSolidOn());
    public static final DeferredBlock<Block> METEORITE_STONE_SLAB = BLOCKS.registerBlock("meteorite_stone_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(METEORITE_STONE.get()));
    public static final DeferredBlock<Block> METEORITE_BRICKS = BLOCKS.registerSimpleBlock("meteorite_bricks", p -> p
            .mapColor(MapColor.COLOR_BLACK)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(4.0F, 8.0F)
            .sound(SoundType.DEEPSLATE)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredBlock<Block> METEORITE_BRICK_STAIRS = registerStairs("meteorite_brick_stairs", METEORITE_BRICKS);
    public static final DeferredBlock<Block> METEORITE_BRICK_WALL = BLOCKS.registerBlock("meteorite_brick_wall", WallBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(METEORITE_BRICKS.get()).forceSolidOn());
    public static final DeferredBlock<Block> METEORITE_BRICK_SLAB = BLOCKS.registerBlock("meteorite_brick_slab", SlabBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(METEORITE_BRICKS.get()));
    public static final DeferredBlock<Block> METEORITE_COAL_ORE = BLOCKS.registerBlock("meteorite_coal_ore", p -> new DropExperienceBlock(UniformInt.of(0, 2), p), (p) -> BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_ORE)
            .mapColor(MapColor.COLOR_BLACK).strength(5.5F, 4F).sound(SoundType.DEEPSLATE));
    public static final DeferredBlock<Block> METEORITE_IRON_ORE = BLOCKS.registerBlock("meteorite_iron_ore", p -> new DropExperienceBlock(ConstantInt.of(0), p), (p) -> BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_ORE)
            .mapColor(MapColor.COLOR_BLACK).strength(5.5F, 4F).sound(SoundType.DEEPSLATE));
    public static final DeferredBlock<Block> METEORITE_COPPER_ORE = BLOCKS.registerBlock("meteorite_copper_ore", p -> new DropExperienceBlock(ConstantInt.of(0), p), (p) -> BlockBehaviour.Properties.ofFullCopy(Blocks.COPPER_ORE)
            .mapColor(MapColor.COLOR_BLACK).strength(5.5F, 4F).sound(SoundType.DEEPSLATE));
    public static final DeferredBlock<Block> METEORITE_GOLD_ORE = BLOCKS.registerBlock("meteorite_gold_ore", p -> new DropExperienceBlock(ConstantInt.of(0), p), (p) -> BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_ORE)
            .mapColor(MapColor.COLOR_BLACK).strength(5.5F, 4F).sound(SoundType.DEEPSLATE));
    public static final DeferredBlock<Block> METEORITE_REDSTONE_ORE = BLOCKS.registerBlock("meteorite_redstone_ore", RedStoneOreBlock::new, (p) -> BlockBehaviour.Properties.ofFullCopy(Blocks.REDSTONE_ORE)
            .mapColor(MapColor.COLOR_BLACK).strength(5.5F, 4F).sound(SoundType.DEEPSLATE));
    public static final DeferredBlock<Block> METEORITE_EMERALD_ORE = BLOCKS.registerBlock("meteorite_emerald_ore", p -> new DropExperienceBlock(UniformInt.of(3, 7), p), (p) -> BlockBehaviour.Properties.ofFullCopy(Blocks.EMERALD_ORE)
            .mapColor(MapColor.COLOR_BLACK).strength(5.5F, 4F).sound(SoundType.DEEPSLATE));
    public static final DeferredBlock<Block> METEORITE_LAPIS_ORE = BLOCKS.registerBlock("meteorite_lapis_ore", p -> new DropExperienceBlock(UniformInt.of(2, 5), p), (p) -> BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_ORE)
            .mapColor(MapColor.COLOR_BLACK).strength(5.5F, 4F).sound(SoundType.DEEPSLATE));
    public static final DeferredBlock<Block> METEORITE_DIAMOND_ORE = BLOCKS.registerBlock("meteorite_diamond_ore", p -> new DropExperienceBlock(UniformInt.of(3, 7), p), (p) -> BlockBehaviour.Properties.ofFullCopy(Blocks.DIAMOND_ORE)
            .mapColor(MapColor.COLOR_BLACK).strength(5.5F, 4F).sound(SoundType.DEEPSLATE));
    public static final DeferredBlock<Block> METEORITE_VIBRANIUM_ORE = BLOCKS.registerBlock("meteorite_vibranium_ore", p -> new DropExperienceBlock(UniformInt.of(5, 9), p), p -> p
            .mapColor(MapColor.COLOR_BLACK)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(30F, 1200.0F)
            .sound(SoundType.DEEPSLATE)
            .lightLevel(value -> 4)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredBlock<Block> METEORITE_VIBRANIUM_VEIN = BLOCKS.registerSimpleBlock("meteorite_vibranium_vein", (p) -> BlockBehaviour.Properties.ofFullCopy(METEORITE_STONE.get()));
    public static final DeferredBlock<Block> VIBRANIUM_BLOCK = BLOCKS.registerSimpleBlock("vibranium_block", p -> p.mapColor(MapColor.COLOR_LIGHT_BLUE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(50F, 1200.0F)
            .sound(SoundType.NETHERITE_BLOCK)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredBlock<Block> RAW_VIBRANIUM_BLOCK = BLOCKS.registerSimpleBlock("raw_vibranium_block", p -> p
            .mapColor(MapColor.COLOR_BLACK)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(30F, 1200.0F)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));

    private static DeferredBlock<Block> registerStairs(String name, Supplier<Block> baseBlock) {
        return BLOCKS.registerBlock(name, props -> new StairBlock(baseBlock.get().defaultBlockState(), props), () -> BlockBehaviour.Properties.ofFullCopy(baseBlock.get()));
    }

}
