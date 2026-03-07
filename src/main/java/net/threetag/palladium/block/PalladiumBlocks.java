package net.threetag.palladium.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.flag.PalladiumFeatureFlags;

public class PalladiumBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Palladium.MOD_ID);

    // Utilities
    public static final DeferredBlock<Block> TAILORING_BENCH = BLOCKS.registerBlock("tailoring_bench", TailoringBenchBlock::new, p -> p.requiredFeatures(PalladiumFeatureFlags.TAILORING));

    // Resources
    public static final DeferredBlock<Block> METEORITE_STONE = BLOCKS.registerSimpleBlock("meteorite_stone", p -> p.mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(4.0F, 8.0F)
            .sound(SoundType.DEEPSLATE)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredBlock<Block> METEORITE_BRICKS = BLOCKS.registerSimpleBlock("meteorite_bricks", p -> p.mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(4.0F, 8.0F)
            .sound(SoundType.DEEPSLATE)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredBlock<Block> VIBRANIUM_ORE = BLOCKS.registerSimpleBlock("vibranium_ore", p -> p.mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(30F, 1200.0F)
            .sound(SoundType.DEEPSLATE)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredBlock<Block> VIBRANIUM_BLOCK = BLOCKS.registerSimpleBlock("vibranium_block", p -> p.mapColor(MapColor.COLOR_LIGHT_BLUE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(50F, 1200.0F)
            .sound(SoundType.NETHERITE_BLOCK)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredBlock<Block> RAW_VIBRANIUM_BLOCK = BLOCKS.registerSimpleBlock("raw_vibranium_block", p -> p.mapColor(MapColor.DEEPSLATE)
            .instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()
            .strength(30F, 1200.0F)
            .requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));

}
