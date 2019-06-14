package com.threetag.threecore.base.inventory;

import com.threetag.threecore.base.ThreeCoreBase;
import com.threetag.threecore.base.recipe.GrinderRecipe;
import com.threetag.threecore.base.tileentity.GrinderTileEntity;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.renderer.tileentity.BellTileEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.IRecipeContainer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;

public class GrinderContainer extends RecipeBookContainer implements IRecipeContainer {

    public final PlayerInventory inventoryPlayer;
    public final GrinderTileEntity grinderTileEntity;
    private final IIntArray intArray;
    public RecipeWrapper invWrapper;
    protected final World world;

    public GrinderContainer(int id, PlayerInventory inventoryPlayer) {
        this(id, inventoryPlayer, new GrinderTileEntity(), new IntArray(4));
    }

    public GrinderContainer(int id, PlayerInventory inventoryPlayer, GrinderTileEntity grinderTileEntity, IIntArray intArray) {
        super(ThreeCoreBase.GRINDER_CONTAINER, id);
        this.inventoryPlayer = inventoryPlayer;
        this.grinderTileEntity = grinderTileEntity;
        this.grinderTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler -> invWrapper = new RecipeWrapper((IItemHandlerModifiable) itemHandler)));
        this.world = inventoryPlayer.player.world;
        func_216962_a(this.invWrapper, 4);
        func_216959_a(intArray, 4);
        this.intArray = intArray;
        this.func_216961_a(this.intArray);

        this.grinderTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 8, 61));
            this.addSlot(new SlotItemHandler(itemHandler, 1, 44, 39));
            this.addSlot(new GrinderResultSlot(inventoryPlayer.player, grinderTileEntity, itemHandler, 2, 104, 39));
            this.addSlot(new SlotItemHandler(itemHandler, 3, 133, 39));
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 92 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventoryPlayer, k, 8 + k * 18, 150));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.grinderTileEntity.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2 || index == 3) {
                if (!this.mergeItemStack(itemstack1, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (itemstack1.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (this.canWork(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 4 && index < 31) {
                    if (!this.mergeItemStack(itemstack1, 31, 40, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 31 && index < 40 && !this.mergeItemStack(itemstack1, 4, 31, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 4, 40, false)) {
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

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    private boolean canWork(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe(GrinderRecipe.RECIPE_TYPE, new Inventory(new ItemStack[]{stack}), this.grinderTileEntity.getWorld()).isPresent();
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

//        if (!grinderTileEntity.getWorld().isRemote) {
//            if (grinderTileEntity.progress != grinderTileEntity.clientProgress || grinderTileEntity.progressMax != grinderTileEntity.clientProgressMax || grinderTileEntity.getEnergy() != grinderTileEntity.clientEnergy) {
//                grinderTileEntity.clientEnergy = grinderTileEntity.getEnergy();
//                grinderTileEntity.clientProgress = grinderTileEntity.progress;
//                grinderTileEntity.clientProgressMax = grinderTileEntity.progressMax;

        // TODO container listener syncing
//                for (IContainerListener listener : this.list) {
//                    if (listener instanceof ServerPlayerEntity) {
//                        ServerPlayerEntity player = (ServerPlayerEntity) listener;
//                        CompoundNBT nbt = new CompoundNBT();
//                        nbt.putInt("Progress", grinderTileEntity.progress);
//                        nbt.putInt("ProgressMax", grinderTileEntity.progressMax);
//                        nbt.putInt("Energy", grinderTileEntity.getEnergy());
//                        nbt.putInt("EnergyMax", grinderTileEntity.getMaxEnergy());
//                        ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSyncTileEntity(nbt), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
//                    }
//                }
//            }
//        }
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
    public CraftResultInventory getCraftResult() {
        return null;
    }

    @Override
    public CraftingInventory getCraftMatrix() {
        return null;
    }

    @Override
    public void func_201771_a(RecipeItemHelper recipeItemHelper) {
        if (this.grinderTileEntity instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator) this.grinderTileEntity).fillStackedContents(recipeItemHelper);
        }
    }

    @Override
    public void clear() {
        this.grinderTileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler -> {
            if (itemHandler instanceof IItemHandlerModifiable) {
                for (int i = 0; i < itemHandler.getSlots(); i++) {
                    ((IItemHandlerModifiable) itemHandler).setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }));
    }

    @Override
    public boolean matches(IRecipe recipe) {
        return recipe.matches(this.invWrapper, this.inventoryPlayer.player.world);
    }

    @Override
    public int getOutputSlot() {
        return 1;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 3;
    }

    public static class GrinderResultSlot extends SlotItemHandler {

        private final PlayerEntity player;
        private final TileEntity tileEntity;
        private int removeCount;

        public GrinderResultSlot(PlayerEntity player, TileEntity tileEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.player = player;
            this.tileEntity = tileEntity;
        }

        @Nonnull
        @Override
        public ItemStack decrStackSize(int amount) {
            if (this.getHasStack()) {
                this.removeCount += Math.min(amount, this.getStack().getCount());
            }
            return super.decrStackSize(amount);
        }

        @Override
        public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
            this.onCrafting(stack);
            super.onTake(thePlayer, stack);
            return stack;
        }

        @Override
        protected void onCrafting(ItemStack stack, int amount) {
            this.removeCount += amount;
            this.onCrafting(stack);
        }

        @Override
        protected void onCrafting(ItemStack stack) {
            stack.onCrafting(this.player.world, this.player, this.removeCount);
            if (!this.player.world.isRemote && this.inventory instanceof GrinderTileEntity) {
                ((GrinderTileEntity) this.inventory).unlockRecipes(this.player);
            }

            this.removeCount = 0;
        }
    }

}
