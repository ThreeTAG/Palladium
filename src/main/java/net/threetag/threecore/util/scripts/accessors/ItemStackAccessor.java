package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.item.ItemStack;
import net.threetag.threecore.util.scripts.ScriptParameterName;

public class ItemStackAccessor extends ScriptAccessor<ItemStack> {

    public static final ItemStackAccessor EMPTY = new ItemStackAccessor(ItemStack.EMPTY);

    public ItemStackAccessor(ItemStack value) {
        super(value);
    }

    public String getItem() {
        return this.value.getItem().getRegistryName().toString();
    }

    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    public int getCount() {
        return this.value.getCount();
    }

    public ItemStackAccessor setCount(@ScriptParameterName("count") int count) {
        this.value.setCount(count);
        return this;
    }

    public int getMaxStackSize() {
        return this.value.getMaxStackSize();
    }

    public int getDamage() {
        return this.value.getDamage();
    }

    public int getMaxDamage() {
        return this.value.getMaxDamage();
    }

    public boolean isDamaged() {
        return this.value.isDamaged();
    }

    public ItemStackAccessor setDamage(@ScriptParameterName("damage") int damage) {
        this.value.setDamage(damage);
        return this;
    }

    public ItemStackAccessor split(@ScriptParameterName("amount") int amount) {
        return new ItemStackAccessor(this.value.split(amount));
    }

    public ItemStackAccessor copy() {
        return new ItemStackAccessor(this.value.copy());
    }

    public CompoundNBTAccessor getNbtTag() {
        return new CompoundNBTAccessor(this.value.getOrCreateTag());
    }

}
