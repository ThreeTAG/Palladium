package net.threetag.palladium.datagen.internal;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;

public class PalladiumModelProvider extends ModelProvider {

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

        // Items
        itemModels.generateFlatItem(PalladiumItems.SUIT_STAND.get(), ModelTemplates.FLAT_ITEM);
        for (DeferredItem<Item> item : PalladiumItems.FABRIC_BY_COLOR.values()) {
            itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
        }
    }
}
