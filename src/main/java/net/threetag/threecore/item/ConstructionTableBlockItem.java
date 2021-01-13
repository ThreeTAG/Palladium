package net.threetag.threecore.item;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.threetag.threecore.util.ItemGroupFiller;

public class ConstructionTableBlockItem extends BlockItem {

    public static final ItemGroupFiller FILLER = new ItemGroupFiller(Blocks.STONECUTTER::asItem);

    public ConstructionTableBlockItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.isInGroup(group)) {
            FILLER.fill(items, new ItemStack(this));
        }
    }
}
