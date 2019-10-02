package net.threetag.threecore.util.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.function.BiConsumer;

public class ItemStackHandlerExt extends ItemStackHandler {

    private TriPredicate<ItemStackHandlerExt, Integer, ItemStack> validator;
    private BiConsumer<ItemStackHandlerExt, Integer> changedCallback;

    public ItemStackHandlerExt() {
    }

    public ItemStackHandlerExt(int size) {
        super(size);
    }

    public ItemStackHandlerExt(NonNullList<ItemStack> stacks) {
        super(stacks);
    }

    public ItemStackHandlerExt setValidator(TriPredicate<ItemStackHandlerExt, Integer, ItemStack> validator) {
        this.validator = validator;
        return this;
    }

    public ItemStackHandlerExt setChangedCallback(BiConsumer<ItemStackHandlerExt, Integer> changedCallback) {
        this.changedCallback = changedCallback;
        return this;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.validator == null || this.validator.test(this, slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.changedCallback != null)
            this.changedCallback.accept(this, slot);
    }
}
