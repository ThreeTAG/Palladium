package net.threetag.palladium.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class SortedItem extends Item {

    public final CreativeModeTabFiller filler;

    public SortedItem(Properties properties, CreativeModeTabFiller filler) {
        super(properties);
        this.filler = filler;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (this.allowdedIn(category)) {
            this.filler.fill(this, category, items);
        }
    }
}
