package net.threetag.palladium.item;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class CreativeModeTabFiller {

    private final Supplier<ItemLike> targetItem;
    private final Map<CreativeModeTab, OffsetValue> offsetMap = Maps.newHashMap();

    public CreativeModeTabFiller(Supplier<ItemLike> itemProvider) {
        this.targetItem = itemProvider;
    }

    public void fill(Item item, CreativeModeTab group, NonNullList<ItemStack> items) {
        OffsetValue offset = this.offsetMap.computeIfAbsent(group, (key) -> new OffsetValue());
        if (offset.itemsProcessed.contains(item)) {
            offset.reset();
        }
        int index = findIndexOfItem(this.targetItem.get().asItem(), items);
        if (index != -1) {
            items.add(index + offset.offset, new ItemStack(item));
            offset.itemsProcessed.add(item);
            offset.offset++;
        } else {
            items.add(new ItemStack(item));
        }
    }

    public static int findIndexOfItem(Item item, NonNullList<ItemStack> items) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    static class OffsetValue {
        private final Set<Item> itemsProcessed = Sets.newHashSet();
        private int offset = 1;

        private void reset() {
            this.offset = 1;
            this.itemsProcessed.clear();
        }
    }

}
