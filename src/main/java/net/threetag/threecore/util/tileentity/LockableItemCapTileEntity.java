package net.threetag.threecore.util.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class LockableItemCapTileEntity extends LockableTileEntity {

    public LockableItemCapTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public int getSizeInventory() {
        AtomicInteger integer = new AtomicInteger(0);
        this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            integer.set(itemHandler.getSlots());
        });
        return integer.get();
    }

    @Override
    public boolean isEmpty() {
        if (!this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
            return false;
        IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (!this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
            return ItemStack.EMPTY;
        IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        return itemHandler.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        if (!this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
            return ItemStack.EMPTY;
        IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        ItemStack stack = itemHandler.getStackInSlot(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (!this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
            return ItemStack.EMPTY;
        IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        return itemHandler.extractItem(index, itemHandler.getSlotLimit(index), false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (!this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent())
            return;
        IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (itemHandler instanceof IItemHandlerModifiable) {
            ((IItemHandlerModifiable) itemHandler).setStackInSlot(index, stack);
        }
    }

    @Override
    public void clear() {
        this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            if (itemHandler instanceof IItemHandlerModifiable) {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    ((IItemHandlerModifiable) itemHandler).setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        });
    }
}
