package net.threetag.palladium.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.threetag.palladium.PalladiumConfig;

public class ExperimentalBlockItem extends BlockItem {

    public ExperimentalBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (PalladiumConfig.Server.EXPERIMENTAL_FEATURES.get()) {
            super.fillItemCategory(category, items);
        }
    }
}
