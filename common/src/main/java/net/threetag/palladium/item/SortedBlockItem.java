package net.threetag.palladium.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class SortedBlockItem extends BlockItem {

    public final CreativeModeTabFiller filler;

    public SortedBlockItem(Block block, Properties properties, CreativeModeTabFiller filler) {
        super(block, properties);
        this.filler = filler;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (this.allowedIn(category)) {
            this.filler.fill(this, category, items);
        }
    }
}
