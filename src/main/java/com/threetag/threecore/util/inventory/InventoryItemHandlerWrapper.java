package com.threetag.threecore.util.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

public class InventoryItemHandlerWrapper implements IInventory {

    public final IItemHandler itemHandler;
    private int slotLimit;

    public InventoryItemHandlerWrapper(IItemHandler itemHandler) {
        this.itemHandler = itemHandler;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            this.slotLimit = Math.max(this.slotLimit, itemHandler.getSlotLimit(i));
        }
    }

    @Override
    public int getSizeInventory() {
        return this.itemHandler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!this.itemHandler.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.itemHandler.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return this.itemHandler.extractItem(index, getStackInSlot(index).getCount(), false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (this.itemHandler instanceof IItemHandlerModifiable)
            ((IItemHandlerModifiable) this.itemHandler).setStackInSlot(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.slotLimit;
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.itemHandler.isItemValid(index, stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        if (this.itemHandler instanceof IItemHandlerModifiable) {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ((IItemHandlerModifiable) itemHandler).setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public ITextComponent getName() {
        return new TextComponentString("Item Handler Inventory Wrapper");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }
}
