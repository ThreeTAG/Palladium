package net.threetag.threecore.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Iterator;

public class ConstructionTableInventory implements IInventory {

    private final NonNullList<ItemStack> stackList;
    private final Container container;

    public ConstructionTableInventory(Container container, int size) {
        this.stackList = NonNullList.withSize(size, ItemStack.EMPTY);
        this.container = container;
    }

    public ItemStack getToolItem() {
        return this.getStackInSlot(this.getSizeInventory() - 1);
    }

    @Override
    public int getSizeInventory() {
        return this.stackList.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator iterator = this.stackList.iterator();

        ItemStack stack;
        do {
            if (!iterator.hasNext()) {
                return true;
            }

            stack = (ItemStack) iterator.next();
        } while (stack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : this.stackList.get(index);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.stackList, index);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        ItemStack stack = ItemStackHelper.getAndSplit(this.stackList, index, amount);
        if (!stack.isEmpty()) {
            this.container.onCraftMatrixChanged(this);
        }

        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.stackList.set(index, stack);
        this.container.onCraftMatrixChanged(this);
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.stackList.clear();
    }

}
