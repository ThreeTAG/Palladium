package net.threetag.threecore.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.threetag.threecore.tileentity.FluidComposerTileEntity;

public class FluidComposerContainer extends Container {

    public final PlayerInventory inventoryPlayer;
    public final FluidComposerTileEntity fluidComposerTileEntity;
    private final IIntArray intArray;
    public RecipeWrapper recipeWrapper;
    protected final World world;

    public FluidComposerContainer(int id, PlayerInventory inventoryPlayer) {
        this(id, inventoryPlayer, new FluidComposerTileEntity(), new IntArray(4));
    }

    public FluidComposerContainer(int id, PlayerInventory inventoryPlayer, FluidComposerTileEntity fluidComposerTileEntity) {
        this(id, inventoryPlayer, fluidComposerTileEntity, new IntArray(4));
    }

    public FluidComposerContainer(int id, PlayerInventory inventoryPlayer, FluidComposerTileEntity fluidComposerTileEntity, IIntArray intArray) {
        super(TCContainerTypes.FLUID_COMPOSER.get(), id);

        this.inventoryPlayer = inventoryPlayer;
        this.fluidComposerTileEntity = fluidComposerTileEntity;
        this.recipeWrapper = fluidComposerTileEntity.recipeWrapper;
        this.world = inventoryPlayer.player.world;
        assertInventorySize(this.recipeWrapper, 9);
        assertIntArraySize(intArray, 4);
        this.intArray = intArray;
        this.trackIntArray(this.intArray);

        this.fluidComposerTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 48, 95));
            this.addSlot(new SlotItemHandler(itemHandler, 1, 8, 17));
            this.addSlot(new SlotItemHandler(itemHandler, 2, 8, 103));
            this.addSlot(new SlotItemHandler(itemHandler, 3, 152, 17));
            this.addSlot(new SlotItemHandler(itemHandler, 4, 152, 103));

            int o = 5;
            for (int y = 0; y < 3; y++) {
                for (int x = 0; x < 3; x++) {
                    this.addSlot(new SlotItemHandler(itemHandler, o, 46 + x * 18, 29 + y * 18));
                    o++;
                }
            }

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
        return this.fluidComposerTileEntity.isUsableByPlayer(playerIn);
    }

    @OnlyIn(Dist.CLIENT)
    public int getProgressScaled(int width) {
        int progress = this.intArray.get(0);
        int maxProgress = this.intArray.get(1);
        return maxProgress != 0 && progress != 0 ? progress * width / maxProgress : 0;
    }

    @OnlyIn(Dist.CLIENT)
    public float getEnergyPercentage() {
        return (float) this.intArray.get(2) / (float) this.intArray.get(3);
    }

    @OnlyIn(Dist.CLIENT)
    public int getEnergyStored() {
        return this.intArray.get(2);
    }

    @OnlyIn(Dist.CLIENT)
    public int getMaxEnergyStored() {
        return this.intArray.get(3);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            if (index >= 14) {
                // TODO this is kinda weird
                if (stack1.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).isPresent()) {
                    if (FluidUtil.tryEmptyContainer(stack1, this.fluidComposerTileEntity.inputFluidTank, Integer.MAX_VALUE, null, false).isSuccess()) {
                        if (!this.mergeItemStack(stack1, 1, 2, false)) {
                            return ItemStack.EMPTY;
                        }
                    }

                    if (FluidUtil.tryEmptyContainer(stack1, this.fluidComposerTileEntity.outputFluidTank, Integer.MAX_VALUE, null, false).isSuccess()) {
                        if (!this.mergeItemStack(stack1, 3, 4, false)) {
                            return ItemStack.EMPTY;
                        }
                    }

                    if (FluidUtil.tryFillContainer(stack1, this.fluidComposerTileEntity.inputFluidTank, Integer.MAX_VALUE, null, false).isSuccess()) {
                        if (!this.mergeItemStack(stack1, 2, 3, false)) {
                            return ItemStack.EMPTY;
                        }
                    }

                    if(FluidUtil.tryFillContainer(stack1, this.fluidComposerTileEntity.outputFluidTank, Integer.MAX_VALUE, null, false).isSuccess()) {
                        if (!this.mergeItemStack(stack1, 4, 5, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                } else if (stack1.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                    if (!this.mergeItemStack(stack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!isFull(this.recipeWrapper)) {
                    if (!this.mergeItemStack(stack1, 5, 14, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 14 && index < 41) {
                    if (!this.mergeItemStack(stack1, 41, 50, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 41 && index < 50 && !this.mergeItemStack(stack1, 14, 41, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, 14, 50, false)) {
                return ItemStack.EMPTY;
            }

            if (stack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack1);
        }

        return stack;
    }

    public boolean isFull(IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }

        return true;
    }

}
