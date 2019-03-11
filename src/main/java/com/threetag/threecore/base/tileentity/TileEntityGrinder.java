package com.threetag.threecore.base.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.base.ThreeCoreBase;
import com.threetag.threecore.base.block.BlockGrinder;
import com.threetag.threecore.base.inventory.ContainerGrinder;
import com.threetag.threecore.base.inventory.GuiGrinder;
import com.threetag.threecore.base.recipe.GrinderRecipe;
import com.threetag.threecore.util.gui.IGuiTile;
import com.threetag.threecore.util.inventory.InventoryItemHandlerWrapper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TileEntityGrinder extends TileEntity implements ITickable, IInteractionObject, IRecipeHolder, IRecipeHelperPopulator, IGuiTile {

    private final Map<ResourceLocation, Integer> recipeUseCounts = Maps.newHashMap();
    private ITextComponent customName;
    private EnergyStorage energyStorage = new EnergyStorage(4000, 128, 128);
    public int progress;
    public int progressMax;
    public int clientProgress;
    public int clientProgressMax;
    public int clientEnergy;
    public int clientEnergyMax;
    private ItemStackHandler energySlot = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
        }

        @Override
        protected void onContentsChanged(int slot) {
            TileEntityGrinder.this.markDirty();
        }
    };
    private ItemStackHandler inputSlot = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityGrinder.this.markDirty();
        }
    };
    private ItemStackHandler outputSlots = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            TileEntityGrinder.this.markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }
    };
    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(energySlot, inputSlot, outputSlots);
    private InventoryItemHandlerWrapper invWrapper = new InventoryItemHandlerWrapper(this.combinedHandler);

    public TileEntityGrinder() {
        super(ThreeCoreBase.TYPE_GRINDER);
    }

    @Override
    public void read(NBTTagCompound nbt) {
        super.read(nbt);

        this.progress = nbt.getInt("Progres");
        this.progressMax = nbt.getInt("ProgressMax");
        this.energyStorage = new EnergyStorage(4000, 128, 128, nbt.getInt("Energy"));

        if (nbt.contains("EnergySlots"))
            energySlot.deserializeNBT((NBTTagCompound) nbt.get("EnergySlots"));
        if (nbt.contains("InputSlots"))
            inputSlot.deserializeNBT((NBTTagCompound) nbt.get("InputSlots"));
        if (nbt.contains("OutputSlots"))
            outputSlots.deserializeNBT((NBTTagCompound) nbt.get("OutputSlots"));

        int i = nbt.getShort("RecipesUsedSize");
        for (int j = 0; j < i; ++j) {
            ResourceLocation resourcelocation = new ResourceLocation(nbt.getString("RecipeLocation" + j));
            int k = nbt.getInt("RecipeAmount" + j);
            this.recipeUseCounts.put(resourcelocation, k);
        }

        if (nbt.contains("CustomName", 8))
            this.customName = ITextComponent.Serializer.fromJson(nbt.getString("CustomName"));
    }

    @Override
    public NBTTagCompound write(NBTTagCompound nbt) {
        super.write(nbt);

        nbt.putInt("Progress", this.progress);
        nbt.putInt("ProgressMax", this.progressMax);
        nbt.putInt("Energy", this.energyStorage.getEnergyStored());
        nbt.put("EnergySlots", energySlot.serializeNBT());
        nbt.put("InputSlots", inputSlot.serializeNBT());
        nbt.put("OutputSlots", outputSlots.serializeNBT());

        nbt.putShort("RecipesUsedSize", (short) this.recipeUseCounts.size());
        int i = 0;

        for (Map.Entry<ResourceLocation, Integer> entry : this.recipeUseCounts.entrySet()) {
            nbt.putString("RecipeLocation" + i, entry.getKey().toString());
            nbt.putInt("RecipeAmount" + i, entry.getValue());
            ++i;
        }

        if (this.customName != null)
            nbt.putString("CustomName", ITextComponent.Serializer.toJson(this.customName));
        return nbt;
    }

    @Override
    public void tick() {
        boolean working = this.isWorking();
        boolean dirty = false;

        if (!this.world.isRemote) {
            this.energySlot.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> {
                int energy = e.extractEnergy(e.getEnergyStored(), true);
                energy = energyStorage.receiveEnergy(energy, true);
                if (energy > 0) {
                    this.energyStorage.receiveEnergy(energy, false);
                    e.extractEnergy(energy, false);
                }
            });

            ItemStack input = this.inputSlot.getStackInSlot(0);
            if (!input.isEmpty()) {
                GrinderRecipe recipe = this.world.getRecipeManager().getRecipe(this.invWrapper, this.world, GrinderRecipe.RECIPE_TYPE);
                if (canWork(recipe)) {
                    this.progressMax = recipe.getEnergy();
                    if (progress >= progressMax) {
                        produceOutput(recipe);
                        progress = 0;
                        dirty = true;
                    } else {
                        progress++;
                        this.energyStorage.extractEnergy(1, false);
                    }
                } else {
                    progress = 0;
                }
            } else {
                progress = 0;
            }
        }

        if (working != this.isWorking()) {
            dirty = true;
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(BlockGrinder.LIT, Boolean.valueOf(this.isWorking())), 3);
        }

        if (dirty)
            this.markDirty();
    }

    public boolean isWorking() {
        return this.progress > 0;
    }

    public boolean canWork(GrinderRecipe recipe) {
        if (recipe != null && this.energyStorage.extractEnergy(1, true) == 1) {
            ItemStack recipeOutput = recipe.getRecipeOutput();
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                boolean output;
                boolean byproduct;

                ItemStack outputSlot = this.outputSlots.getStackInSlot(0);
                if (outputSlot.isEmpty()) {
                    output = true;
                } else if (!outputSlot.isItemEqual(recipeOutput)) {
                    output = false;
                } else if (outputSlot.getCount() + recipeOutput.getCount() <= this.outputSlots.getSlotLimit(0) && outputSlot.getCount() < outputSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    output = true;
                } else {
                    output = outputSlot.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }

                ItemStack byproductSlot = this.outputSlots.getStackInSlot(1);
                ItemStack byproductItem = recipe.getByproduct();
                if (byproductSlot.isEmpty() || byproductItem.isEmpty()) {
                    byproduct = true;
                } else if (!byproductSlot.isItemEqual(byproductItem)) {
                    byproduct = false;
                } else if (byproductSlot.getCount() + byproductItem.getCount() <= this.outputSlots.getSlotLimit(0) && byproductSlot.getCount() < byproductSlot.getMaxStackSize()) { // Forge fix: make furnace respect stack sizes in furnace recipes
                    byproduct = true;
                } else {
                    byproduct = byproductSlot.getCount() + byproductItem.getCount() <= byproductItem.getMaxStackSize(); // Forge fix: make furnace respect stack sizes in furnace recipes
                }

                return output && byproduct;
            }
        } else {
            return false;
        }
    }

    public void produceOutput(GrinderRecipe recipe) {
        if (recipe != null && this.canWork(recipe)) {
            ItemStack recipeOutput = recipe.getRecipeOutput();
            ItemStack outputSlot = outputSlots.getStackInSlot(0);
            if (outputSlot.isEmpty()) {
                this.outputSlots.setStackInSlot(0, recipeOutput.copy());
            } else if (outputSlot.getItem() == recipeOutput.getItem()) {
                outputSlot.grow(recipeOutput.getCount());
            }

            if (!recipe.getByproduct().isEmpty() && recipe.getByproductChance() > new Random().nextFloat()) {
                ItemStack byproductSlot = outputSlots.getStackInSlot(1);
                if (byproductSlot.isEmpty()) {
                    this.outputSlots.setStackInSlot(1, recipe.getByproduct().copy());
                } else if (byproductSlot.getItem() == recipe.getByproduct().getItem()) {
                    byproductSlot.grow(recipe.getByproduct().getCount());
                }
            }

            if (!this.world.isRemote) {
                this.canUseRecipe(this.world, (EntityPlayerMP) null, recipe);
            }

            this.inputSlot.getStackInSlot(0).shrink(1);
        }
    }

    public boolean isUsableByPlayer(EntityPlayer player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerGrinder(inventoryPlayer, this);
    }

    @Override
    public String getGuiID() {
        return ThreeCore.MODID + ":grinder";
    }

    @Override
    public ITextComponent getName() {
        return this.customName != null ? this.customName : new TextComponentTranslation("container.threecore.grinder");
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return this.customName;
    }

    public void setCustomName(@Nullable ITextComponent name) {
        this.customName = name;
    }

    @Override
    public void fillStackedContents(RecipeItemHelper recipeItemHelper) {
        for (int i = 0; i < this.combinedHandler.getSlots(); i++) {
            ItemStack stack = this.combinedHandler.getStackInSlot(i);
            recipeItemHelper.accountStack(stack);
        }
    }

    @Override
    public void setRecipeUsed(@Nullable IRecipe recipe) {
        if (this.recipeUseCounts.containsKey(recipe.getId())) {
            this.recipeUseCounts.put(recipe.getId(), this.recipeUseCounts.get(recipe.getId()) + 1);
        } else {
            this.recipeUseCounts.put(recipe.getId(), 1);
        }
    }

    @Nullable
    public IRecipe getRecipeUsed() {
        return null;
    }

    public Map<ResourceLocation, Integer> getRecipeUseCounts() {
        return this.recipeUseCounts;
    }

    @Override
    public boolean canUseRecipe(World worldIn, EntityPlayerMP player, @Nullable IRecipe recipe) {
        if (recipe != null) {
            this.setRecipeUsed(recipe);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onCrafting(EntityPlayer player) {
        if (!this.world.getGameRules().getBoolean("doLimitedCrafting")) {
            List<IRecipe> list = Lists.newArrayList();

            for (ResourceLocation resourcelocation : this.recipeUseCounts.keySet()) {
                IRecipe irecipe = player.world.getRecipeManager().getRecipe(resourcelocation);
                if (irecipe != null) {
                    list.add(irecipe);
                }
            }

            player.unlockRecipes(list);
        }

        this.recipeUseCounts.clear();
    }

    public int getEnergy() {
        return this.energyStorage.getEnergyStored();
    }

    public int getMaxEnergy() {
        return this.energyStorage.getMaxEnergyStored();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null)
                return LazyOptional.of(() -> (T) combinedHandler);
            else if (side == EnumFacing.UP)
                return LazyOptional.of(() -> (T) inputSlot);
            else if (side == EnumFacing.DOWN)
                return LazyOptional.of(() -> (T) outputSlots);
            else
                return LazyOptional.of(() -> (T) energySlot);
        } else if (cap == CapabilityEnergy.ENERGY)
            return LazyOptional.of(() -> (T) energyStorage);
        return super.getCapability(cap, side);
    }

    @Override
    public GuiContainer createGui(EntityPlayer player) {
        return new GuiGrinder(player.inventory, this);
    }
}
