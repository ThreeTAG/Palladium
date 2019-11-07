package net.threetag.threecore.util.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyLoadBase;

import java.util.function.Supplier;

public class SimpleItemGroup extends ItemGroup {

    private final LazyLoadBase<ItemStack> icon;
    private final String name;

    public SimpleItemGroup(String label, Supplier<ItemStack> icon) {
        super(label);
        this.name = label;
        this.icon = new LazyLoadBase<>(icon);
    }

    public SimpleItemGroup(int index, String label, Supplier<ItemStack> icon) {
        super(index, label);
        this.name = label;
        this.icon = new LazyLoadBase<>(icon);
    }

    public String getName() {
        return this.name;
    }

    @Override
    public ItemStack createIcon() {
        return this.icon.getValue();
    }
}
