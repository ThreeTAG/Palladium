package net.threetag.threecore.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class ItemGroupFiller {

    private final Supplier<Item> item;
    private final List<Item> offsets = new LinkedList<>();

    public ItemGroupFiller(Supplier<Item> item) {
        this.item = item;
    }

    public void fill(NonNullList<ItemStack> items, ItemStack toAdd) {
        int offset;
        if (offsets.contains(toAdd.getItem())) {
            offset = offsets.indexOf(toAdd.getItem());
        } else {
            offsets.add(toAdd.getItem());
            offset = offsets.indexOf(toAdd.getItem());
        }

        boolean found = false;
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (stack.getItem() == this.item.get()) {
                found = true;
                if (i >= items.size() - 1) {
                    items.add(toAdd);
                } else {
                    int index = i + 1 + offset;
                    items.add(index, toAdd);
                }
            }
        }
        if (!found) {
            items.add(toAdd);
        }
    }

}
