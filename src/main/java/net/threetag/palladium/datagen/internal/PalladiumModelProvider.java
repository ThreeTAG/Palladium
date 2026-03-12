package net.threetag.palladium.datagen.internal;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.model.*;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;

public class PalladiumModelProvider extends ModelProvider {

    public static final TextureSlot EMISSIVE_SLOT = TextureSlot.create("emissive");
    public static final ModelTemplate EMISSIVE_OVERLAY = ModelTemplates.create(Palladium.id("cube_emissive_overlay").toString(), TextureSlot.ALL, EMISSIVE_SLOT);

    public PalladiumModelProvider(PackOutput output) {
        super(output, Palladium.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        // Blocks
        blockModels.createHorizontallyRotatedBlock(PalladiumBlocks.TAILORING_BENCH.get(),
                TexturedModel.createDefault(block -> new TextureMapping()
                                .put(TextureSlot.UP, TextureMapping.getBlockTexture(block, "_top"))
                                .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"))
                                .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"))
                                .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"))
                                .put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"))
                                .put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"))
                                .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block, "_side")),
                        ModelTemplates.CUBE));
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_STONE.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_BRICKS.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_COAL_ORE.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_IRON_ORE.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_COPPER_ORE.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_GOLD_ORE.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_REDSTONE_ORE.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_EMERALD_ORE.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_LAPIS_ORE.get());
        blockModels.createTrivialCube(PalladiumBlocks.METEORITE_DIAMOND_ORE.get());
        this.withEmissiveOverlay(blockModels, PalladiumBlocks.METEORITE_VIBRANIUM_ORE.get());
        this.withEmissiveOverlay(blockModels, PalladiumBlocks.METEORITE_VIBRANIUM_VEIN.get());
        blockModels.createTrivialCube(PalladiumBlocks.VIBRANIUM_BLOCK.get());
        blockModels.createTrivialCube(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());

        // Items
        itemModels.generateFlatItem(PalladiumItems.SUIT_STAND.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(PalladiumItems.RAW_VIBRANIUM.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(PalladiumItems.VIBRANIUM_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(PalladiumItems.VIBRANIUM_NUGGET.get(), ModelTemplates.FLAT_ITEM);

        for (DeferredItem<Item> item : PalladiumItems.FABRIC_BY_COLOR.values()) {
            itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
        }
    }

    private void withEmissiveOverlay(BlockModelGenerators blockModels, Block block) {
        MultiVariant multivariant = BlockModelGenerators.plainVariant(
                EMISSIVE_OVERLAY.create(block, TextureMapping.cube(block).put(EMISSIVE_SLOT, TextureMapping.getBlockTexture(block, "_emissive")), blockModels.modelOutput)
        );
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, multivariant));
    }
}
