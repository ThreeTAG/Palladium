package net.threetag.threecore.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;

public class ConstructionTableResultSlot extends Slot {

    private final ConstructionTableInventory craftingInventory;
    private final PlayerEntity player;
    private final IRecipeType recipeType;
    private int amountCrafted;

    public ConstructionTableResultSlot(PlayerEntity player, IRecipeType recipeType, ConstructionTableInventory craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.recipeType = recipeType;
        this.craftingInventory = craftingInventory;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onSwapCraft(int amount) {
        this.amountCrafted += amount;
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.player.world, this.player, this.amountCrafted);
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, stack, this.craftingInventory);
        }

        if (this.inventory instanceof IRecipeHolder) {
            ((IRecipeHolder) this.inventory).onCrafting(this.player);
        }

        this.amountCrafted = 0;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(this.recipeType, this.craftingInventory, thePlayer.world);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack invStack = this.craftingInventory.getStackInSlot(i);
            ItemStack remainingStack = nonnulllist.get(i);

            if (!invStack.isEmpty()) {
                this.craftingInventory.decrStackSize(i, 1);
                invStack = this.craftingInventory.getStackInSlot(i);
            }

            if (!remainingStack.isEmpty()) {
                if (invStack.isEmpty()) {
                    this.craftingInventory.setInventorySlotContents(i, remainingStack);
                } else if (ItemStack.areItemsEqual(invStack, remainingStack) && ItemStack.areItemStackTagsEqual(invStack, remainingStack)) {
                    remainingStack.grow(invStack.getCount());
                    this.craftingInventory.setInventorySlotContents(i, remainingStack);
                } else if (!this.player.inventory.addItemStackToInventory(remainingStack)) {
                    this.player.dropItem(remainingStack, false);
                }
            }
        }
        return stack;
    }
}