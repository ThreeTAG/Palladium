package net.threetag.threecore.util.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.util.threedata.ThreeData;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ThreeDataItemHandler implements IItemHandlerModifiable {

    // TODO make this IThreeDataHolder, overhauling that aswell
    public final Ability dataHolder;
    private final List<ThreeData<ItemStack>> dataList;

    public ThreeDataItemHandler(Ability dataHolder, List<ThreeData<ItemStack>> dataList) {
        this.dataHolder = dataHolder;
        this.dataList = dataList;
    }

    public ThreeDataItemHandler(Ability dataHolder, ThreeData<ItemStack>... data) {
        this.dataHolder = dataHolder;
        this.dataList = Arrays.asList(data);
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= dataList.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + dataList.size() + ")");
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.validateSlotIndex(slot);
        this.dataHolder.getDataManager().set(this.dataList.get(slot), stack);
        this.onContentsChanged(slot);
    }

    @Override
    public int getSlots() {
        return this.dataList.size();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.dataHolder.getDataManager().get(this.dataList.get(slot));
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ThreeData<ItemStack> data = this.dataList.get(slot);
        ItemStack existing = this.dataHolder.getDataManager().get(data);

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                this.dataHolder.getDataManager().set(data, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            this.onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0)
            return ItemStack.EMPTY;

        this.validateSlotIndex(slot);

        ThreeData<ItemStack> data = this.dataList.get(slot);
        ItemStack existing = this.dataHolder.getDataManager().get(data);

        if (existing.isEmpty())
            return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());

        if (existing.getCount() <= toExtract) {
            if (!simulate) {
                this.dataHolder.getDataManager().set(data, ItemStack.EMPTY);
                this.onContentsChanged(slot);
            }
            return existing;
        } else {
            if (!simulate) {
                this.dataHolder.getDataManager().set(data, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                this.onContentsChanged(slot);
            }

            return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    protected void onContentsChanged(int slot) {

    }
}
