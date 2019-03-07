package com.threetag.threecore.base.inventory;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.base.network.MessageSyncTileEntity;
import com.threetag.threecore.base.recipe.GrinderRecipe;
import com.threetag.threecore.base.tileentity.TileEntityGrinder;
import com.threetag.threecore.util.block.ITileEntityListener;
import com.threetag.threecore.util.inventory.InventoryItemHandlerWrapper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.crafting.IRecipeContainer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import java.util.Map;

public class ContainerGrinder extends ContainerRecipeBook implements ITileEntityListener, IRecipeContainer {

    public final InventoryPlayer inventoryPlayer;
    public final TileEntityGrinder tileEntityGrinder;
    public InventoryItemHandlerWrapper invWrapper;

    public ContainerGrinder(InventoryPlayer inventoryPlayer, TileEntityGrinder tileEntityGrinder) {
        this.inventoryPlayer = inventoryPlayer;
        this.tileEntityGrinder = tileEntityGrinder;
        this.tileEntityGrinder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler -> invWrapper = new InventoryItemHandlerWrapper(itemHandler)));

        this.tileEntityGrinder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 8, 61));
            this.addSlot(new SlotItemHandler(itemHandler, 1, 44, 39));
            this.addSlot(new SlotGrinderOutput(inventoryPlayer.player, tileEntityGrinder, itemHandler, 2, 104, 39));
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
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileEntityGrinder.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
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
                if(itemstack1.getCapability(CapabilityEnergy.ENERGY).isPresent()) {
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
        for(IRecipe irecipe : this.tileEntityGrinder.getWorld().getRecipeManager().getRecipes(GrinderRecipe.RECIPE_TYPE)) {
            if (irecipe.getIngredients().get(0).test(stack)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if (!tileEntityGrinder.getWorld().isRemote) {
            if (tileEntityGrinder.progress != tileEntityGrinder.clientProgress || tileEntityGrinder.progressMax != tileEntityGrinder.clientProgressMax || tileEntityGrinder.getEnergy() != tileEntityGrinder.clientEnergy) {
                tileEntityGrinder.clientEnergy = tileEntityGrinder.getEnergy();
                tileEntityGrinder.clientProgress = tileEntityGrinder.progress;
                tileEntityGrinder.clientProgressMax = tileEntityGrinder.progressMax;

                for (IContainerListener listener : listeners) {
                    if (listener instanceof EntityPlayerMP) {
                        EntityPlayerMP player = (EntityPlayerMP) listener;
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.putInt("Progress", tileEntityGrinder.progress);
                        nbt.putInt("ProgressMax", tileEntityGrinder.progressMax);
                        nbt.putInt("Energy", tileEntityGrinder.getEnergy());
                        nbt.putInt("EnergyMax", tileEntityGrinder.getMaxEnergy());
                        ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSyncTileEntity(nbt), player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
                    }
                }
            }
        }
    }

    @Override
    public void sync(NBTTagCompound nbt) {
        this.tileEntityGrinder.clientProgress = nbt.getInt("Progress");
        this.tileEntityGrinder.clientProgressMax = nbt.getInt("ProgressMax");
        this.tileEntityGrinder.clientEnergy = nbt.getInt("Energy");
        this.tileEntityGrinder.clientEnergyMax = nbt.getInt("EnergyMax");
    }

    @Override
    public InventoryCraftResult getCraftResult() {
        return null;
    }

    @Override
    public InventoryCrafting getCraftMatrix() {
        return null;
    }

    @Override
    public void func_201771_a(RecipeItemHelper recipeItemHelper) {
        if (this.tileEntityGrinder instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator) this.tileEntityGrinder).fillStackedContents(recipeItemHelper);
        }
    }

    @Override
    public void clear() {
        this.tileEntityGrinder.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((itemHandler -> {
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

    public static class SlotGrinderOutput extends SlotItemHandler {

        private final EntityPlayer player;
        private final TileEntity tileEntity;
        private int removeCount;

        public SlotGrinderOutput(EntityPlayer player, TileEntity tileEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
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
        public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
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
            if (!this.player.world.isRemote) {
                for (Map.Entry<ResourceLocation, Integer> entry : ((TileEntityGrinder) this.tileEntity).getRecipeUseCounts().entrySet()) {
                    GrinderRecipe recipe = (GrinderRecipe) this.player.world.getRecipeManager().getRecipe(entry.getKey());
                    float f;
                    if (recipe != null) {
                        f = recipe.getExperience();
                    } else {
                        f = 0.0F;
                    }

                    int i = entry.getValue();
                    if (f == 0.0F) {
                        i = 0;
                    } else if (f < 1.0F) {
                        int j = MathHelper.floor((float) i * f);
                        if (j < MathHelper.ceil((float) i * f) && Math.random() < (double) ((float) i * f - (float) j)) {
                            ++j;
                        }

                        i = j;
                    }

                    while (i > 0) {
                        int k = EntityXPOrb.getXPSplit(i);
                        i -= k;
                        this.player.world.spawnEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5D, this.player.posZ + 0.5D, k));
                    }
                }

                ((IRecipeHolder) this.tileEntity).onCrafting(this.player);
            }

            this.removeCount = 0;
        }
    }

}
