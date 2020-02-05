package net.threetag.threecore.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.threetag.threecore.tileentity.StirlingGeneratorTileEntity;

public class StirlingGeneratorContainer extends Container {

    public final PlayerInventory inventoryPlayer;
    public final StirlingGeneratorTileEntity stirlingGeneratorTileEntity;
    private final IIntArray intArray;
    protected final World world;

    public StirlingGeneratorContainer(int id, PlayerInventory inventoryPlayer) {
        this(id, inventoryPlayer, new StirlingGeneratorTileEntity(), new IntArray(4));
    }

    public StirlingGeneratorContainer(int id, PlayerInventory inventoryPlayer, StirlingGeneratorTileEntity stirlingGeneratorTileEntity) {
        this(id, inventoryPlayer, stirlingGeneratorTileEntity, new IntArray(4));
    }

    public StirlingGeneratorContainer(int id, PlayerInventory inventoryPlayer, StirlingGeneratorTileEntity stirlingGeneratorTileEntity, IIntArray intArray) {
        super(TCBaseContainerTypes.STIRLING_GENERATOR, id);

        this.inventoryPlayer = inventoryPlayer;
        this.stirlingGeneratorTileEntity = stirlingGeneratorTileEntity;
        this.world = inventoryPlayer.player.world;
        assertIntArraySize(intArray, 4);
        this.intArray = intArray;
        this.trackIntArray(this.intArray);

        this.stirlingGeneratorTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 67));
            this.addSlot(new SlotItemHandler(itemHandler, 1, 152, 17));
            this.addSlot(new SlotItemHandler(itemHandler, 2, 152, 103));
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 134 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventoryPlayer, k, 8 + k * 18, 192));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.stirlingGeneratorTileEntity.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index > 2) {
                if (AbstractFurnaceTileEntity.isFuel(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() && FluidUtil.tryEmptyContainer(itemstack1, this.stirlingGeneratorTileEntity.fluidTank, Integer.MAX_VALUE, null, false).isSuccess()) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack1.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent() && FluidUtil.tryFillContainer(itemstack1, this.stirlingGeneratorTileEntity.fluidTank, Integer.MAX_VALUE, null, false).isSuccess()) {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled() {
        int i = this.intArray.get(1);
        if (i == 0) {
            i = 200;
        }

        return this.intArray.get(0) * 13 / i;
    }

    @OnlyIn(Dist.CLIENT)
    public float getEnergyPercentage() {
        return (float) this.getEnergyStored() / (float) this.getMaxEnergyStored();
    }

    @OnlyIn(Dist.CLIENT)
    public int getEnergyStored() {
        return this.intArray.get(2);
    }

    @OnlyIn(Dist.CLIENT)
    public int getMaxEnergyStored() {
        return this.intArray.get(3);
    }
}
