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

    public SortedItem(Properties properties) {
        super(properties);
        this.filler = null;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (this.filler != null) {
            if (this.allowedIn(category)) {
                this.filler.fill(this, category, items);
            }
        } else {
            super.fillItemCategory(category, items);
        }
    }
}
