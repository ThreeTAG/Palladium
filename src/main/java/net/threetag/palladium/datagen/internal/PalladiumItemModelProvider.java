package net.threetag.palladium.datagen.internal;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.item.PalladiumItems;

public class PalladiumItemModelProvider extends ModelProvider {

    public PalladiumItemModelProvider(PackOutput output) {
        super(output, Palladium.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(PalladiumItems.SUIT_STAND.get(), ModelTemplates.FLAT_ITEM);
    }
}
