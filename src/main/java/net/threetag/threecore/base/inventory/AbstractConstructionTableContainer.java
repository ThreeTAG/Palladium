package net.threetag.threecore.base.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.threetag.threecore.base.block.TCBaseBlocks;
import net.threetag.threecore.base.recipe.AbstractConstructionTableRecipe;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractConstructionTableContainer<T extends AbstractConstructionTableRecipe> extends Container {

    final ConstructionTableInventory craftingInventory = new ConstructionTableInventory(this, this.getRecipeSerializer().size + 1);
    final CraftResultInventory craftResultInventory = new CraftResultInventory();
    public final IWorldPosCallable worldPosCallable;
    private final PlayerEntity player;

    protected AbstractConstructionTableContainer(@Nullable ContainerType<?> containerType, int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(containerType, id);
        this.worldPosCallable = worldPosCallable;
        this.player = playerInventory.player;
    }

    public abstract AbstractConstructionTableRecipe.Serializer getRecipeSerializer();

    public abstract IRecipeType<T> getRecipeType();

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        this.worldPosCallable.consume((world, blockPos) -> {
            updateRecipe(world);
        });
    }

    protected void updateRecipe(World world) {
        if (!world.isRemote) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ItemStack stack = ItemStack.EMPTY;
            Optional<T> recipe = world.getServer().getRecipeManager().getRecipe(this.getRecipeType(), this.craftingInventory, world);
            if (recipe.isPresent()) {
                T constructionTable = recipe.get();
                if (craftResultInventory.canUseRecipe(world, serverPlayer, constructionTable)) {
                    stack = constructionTable.getCraftingResult(this.craftingInventory);
                }
            }

            craftResultInventory.setInventorySlotContents(0, stack);
            serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, this.inventorySlots.size() - 4 * 9 - 1, stack));
        }
    }

    @Override
    public void onContainerClosed(PlayerEntity player) {
        this.worldPosCallable.consume((world, pos) -> {
            this.clearContainer(player, world, this.craftingInventory);
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(this.worldPosCallable, playerIn, TCBaseBlocks.CONSTRUCTION_TABLE);
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftResultInventory && super.canMergeSlot(stack, slot);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        int shapeSlotCount = this.craftingInventory.getSizeInventory() - 1;
        int upperSlotCount = this.craftingInventory.getSizeInventory() + 1;
        int resultSlotIndex = shapeSlotCount + 1;
        int invBegin = upperSlotCount;
        int invEnd1 = upperSlotCount + 27;
        int invEnd2 = upperSlotCount + 36;

        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack1 = slot.getStack();
            stack = stack1.copy();
            if (index == resultSlotIndex) {
                this.worldPosCallable.consume((world, pos) -> {
                    stack1.getItem().onCreated(stack1, world, player);
                });
                if (!this.mergeItemStack(stack1, invBegin, invEnd2, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(stack1, stack);
            } else if (index >= invBegin && index < invEnd1) {
                if (!this.mergeItemStack(stack1, invEnd1, invEnd2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= invEnd1 && index < invEnd2) {
                if (!this.mergeItemStack(stack1, invBegin, invEnd1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(stack1, invBegin, invEnd2, false)) {
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

            ItemStack lvt_6_1_ = slot.onTake(player, stack1);
            if (index == 0) {
                player.dropItem(lvt_6_1_, false);
            }
        }

        return stack;
    }
}
