package net.threetag.threecore.container;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.item.MultiversalExtrapolatorItem;
import net.threetag.threecore.item.recipe.MultiversalRecipe;

import java.util.List;

public class MultiversalIteratorContainer extends Container {

    private final IWorldPosCallable worldPosCallable;
    private final IntReferenceHolder selectedRecipe = IntReferenceHolder.single();
    private final World world;
    private List<ItemStack> items = Lists.newArrayList();
    private ItemStack itemStackInput = ItemStack.EMPTY;
    private String universe = "";
    private long lastOnTake;
    final Slot extrapolatorInventorySlot;
    final Slot inputInventorySlot;
    final Slot outputInventorySlot;
    private Runnable inventoryUpdateListener = () -> {
    };
    public final IInventory extrapolatorInventory = new Inventory(1) {
        public void markDirty() {
            super.markDirty();
            MultiversalIteratorContainer.this.onCraftMatrixChanged(this);
            MultiversalIteratorContainer.this.inventoryUpdateListener.run();
        }
    };
    public final IInventory inputInventory = new Inventory(1) {
        public void markDirty() {
            super.markDirty();
            MultiversalIteratorContainer.this.onCraftMatrixChanged(this);
            MultiversalIteratorContainer.this.inventoryUpdateListener.run();
        }
    };

    private final CraftResultInventory inventory = new CraftResultInventory();

    public MultiversalIteratorContainer(int windowIdIn, PlayerInventory playerInventoryIn) {
        this(windowIdIn, playerInventoryIn, IWorldPosCallable.DUMMY);
    }

    public MultiversalIteratorContainer(int windowIdIn, PlayerInventory playerInventoryIn, final IWorldPosCallable worldPosCallableIn) {
        super(TCContainerTypes.MULTIVERSAL_ITERATOR.get(), windowIdIn);
        this.worldPosCallable = worldPosCallableIn;
        this.world = playerInventoryIn.player.world;
        this.extrapolatorInventorySlot = this.addSlot(new Slot(this.extrapolatorInventory, 0, 20, 21));
        this.inputInventorySlot = this.addSlot(new Slot(this.inputInventory, 0, 20, 65));
        this.outputInventorySlot = this.addSlot(new Slot(this.inventory, 1, 143, 65) {
            /**
             * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
             */
            public boolean isItemValid(ItemStack stack) {
                return false;
            }

            public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
                ItemStack itemstack = MultiversalIteratorContainer.this.inputInventorySlot.decrStackSize(1);
                if (!itemstack.isEmpty()) {
                    MultiversalIteratorContainer.this.updateRecipeResultSlot();
                }

                stack.getItem().onCreated(stack, thePlayer.world, thePlayer);
                worldPosCallableIn.consume((world, pos) -> {
                    long l = world.getGameTime();
                    if (MultiversalIteratorContainer.this.lastOnTake != l) {
                        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        MultiversalIteratorContainer.this.lastOnTake = l;
                    }

                });
                return super.onTake(thePlayer, stack);
            }
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 116 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 174));
        }

        this.trackInt(this.selectedRecipe);
    }

    @OnlyIn(Dist.CLIENT)
    public int getSelectedRecipe() {
        return this.selectedRecipe.get();
    }

    @OnlyIn(Dist.CLIENT)
    public List<ItemStack> getItemList() {
        return this.items;
    }

    @OnlyIn(Dist.CLIENT)
    public int getRecipeListSize() {
        return this.items.size();
    }

    @OnlyIn(Dist.CLIENT)
    public boolean hasItemsInInputSlot() {
        return this.inputInventorySlot.getHasStack() && !this.items.isEmpty();
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, TCBlocks.MULTIVERSAL_ITERATOR.get());
    }

    @Override
    public boolean enchantItem(PlayerEntity playerIn, int id) {
        id -= 8;
        if (id >= 0 && id < this.items.size()) {
            this.selectedRecipe.set(id);
            this.updateRecipeResultSlot();
        }

        return true;
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        ItemStack itemstack = this.inputInventorySlot.getStack();
        ItemStack extrapolator = this.extrapolatorInventorySlot.getStack();
        if (itemstack.getItem() != this.itemStackInput.getItem() || !this.universe.equals(MultiversalExtrapolatorItem.getUniverse(extrapolator))) {
            this.itemStackInput = itemstack.copy();
            this.universe = MultiversalExtrapolatorItem.getUniverse(extrapolator);
            if (this.universe == null) {
                this.universe = "";
            }
            this.updateAvailableRecipes(inventoryIn, itemstack, this.universe);
        }

    }

    private void updateAvailableRecipes(IInventory inventoryIn, ItemStack stack, String universe) {
        this.items.clear();
        this.selectedRecipe.set(-1);
        this.outputInventorySlot.putStack(ItemStack.EMPTY);
        if (!stack.isEmpty()) {
            this.items = MultiversalRecipe.getVariations(stack, universe, MultiversalRecipe.getIdentifiersFromItem(stack, world), world);
        }

    }

    private void updateRecipeResultSlot() {
        if (!this.items.isEmpty()) {
            ItemStack stack = this.items.get(this.selectedRecipe.get());
            this.outputInventorySlot.putStack(stack.copy());
        } else {
            this.outputInventorySlot.putStack(ItemStack.EMPTY);
        }

        this.detectAndSendChanges();
    }

    @OnlyIn(Dist.CLIENT)
    public void setInventoryUpdateListener(Runnable listenerIn) {
        this.inventoryUpdateListener = listenerIn;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.inventory && super.canMergeSlot(stack, slotIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            Item item = itemstack1.getItem();
            itemstack = itemstack1.copy();
            if (index == 2) {
                item.onCreated(itemstack1, playerIn.world, playerIn);
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index == 1) {
                if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.world.getRecipeManager().getRecipe(IRecipeType.STONECUTTING, new Inventory(itemstack1), this.world).isPresent()) {
                if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 3 && index < 30) {
                if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }

            slot.onSlotChanged();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
            this.detectAndSendChanges();
        }

        return itemstack;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);
        this.inventory.removeStackFromSlot(1);
        this.worldPosCallable.consume((world, pos) -> this.clearContainer(playerIn, playerIn.world, this.inputInventory));
        this.worldPosCallable.consume((world, pos) -> this.clearContainer(playerIn, playerIn.world, this.extrapolatorInventory));
    }
}
