package net.threetag.threecore.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public class SimpleItemGroup extends ItemGroup {

    private final LazyValue<ItemStack> icon;
    private final String name;

    public SimpleItemGroup(String label, Supplier<ItemStack> icon) {
        super(label);
        this.name = label;
        this.icon = new LazyValue<>(icon);
    }

    public SimpleItemGroup(int index, String label, Supplier<ItemStack> icon) {
        super(index, label);
        this.name = label;
        this.icon = new LazyValue<>(icon);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public ItemStack createIcon() {
        return this.icon.getValue();
    }
}
