package net.threetag.threecore.base.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.base.inventory.GrinderContainer;
import net.threetag.threecore.base.recipe.GrinderRecipe;
import net.threetag.threecore.util.energy.IEnergyConfig;
import net.threetag.threecore.util.item.ItemStackHandlerExt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class GrinderTileEntity extends ProgressableMachineTileEntity<GrinderRecipe> {

    protected final IIntArray intArray = new IIntArray() {
        @Override
        public int get(int i) {
            switch (i) {
                case 0:
                    return progress;
                case 1:
                    return maxProgress;
                case 2:
                    return energyStorage.getEnergyStored();
                case 3:
                    return energyStorage.getMaxEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int i, int value) {
            switch (i) {
                case 0:
                    progress = value;
                case 1:
                    maxProgress = value;
                case 2:
                    energyStorage.setEnergyStored(value);
                case 3:
                    energyStorage.setMaxEnergyStored(value);
            }
        }

        @Override
        public int size() {
            return 4;
        }
    };

    private ItemStackHandlerExt energySlot = new ItemStackHandlerExt(1)
            .setValidator((handler, slot, stack) -> stack.getCapability(CapabilityEnergy.ENERGY).isPresent())
            .setChangedCallback((handler, slot) -> GrinderTileEntity.this.markDirty());

    private ItemStackHandlerExt inputSlot = new ItemStackHandlerExt(1)
            .setChangedCallback((handler, slot) -> {
                this.updateRecipe(this.recipeWrapper);
                GrinderTileEntity.this.markDirty();
            });

    private ItemStackHandlerExt outputSlots = new ItemStackHandlerExt(2)
            .setValidator((handler, slot, stack) -> false)
            .setChangedCallback((handler, slot) -> GrinderTileEntity.this.markDirty());

    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(energySlot, inputSlot, outputSlots);
    private RecipeWrapper recipeWrapper = new RecipeWrapper(this.combinedHandler);

    public GrinderTileEntity() {
        super(ThreeCoreBase.GRINDER_TILE_ENTITY);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.threecore.grinder");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new GrinderContainer(id, playerInventory, this, this.intArray);
    }

    @Override
    public IRecipeType<GrinderRecipe> getRecipeType() {
        return GrinderRecipe.RECIPE_TYPE;
    }

    @Override
    public float getXpFromRecipe(GrinderRecipe recipe) {
        return recipe.getExperience();
    }

    @Override
    public IEnergyConfig getEnergyConfig() {
        return ThreeCoreServerConfig.ENERGY.GRINDER;
    }

    @Override
    public IItemHandler getEnergyInputSlots() {
        return this.energySlot;
    }

    @Override
    public boolean canWork(GrinderRecipe recipe) {
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
    }

    @Override
    public void produceOutput(GrinderRecipe recipe) {
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

        this.inputSlot.getStackInSlot(0).shrink(1);
        this.updateRecipe(this.recipeWrapper);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        if (nbt.contains("EnergySlots"))
            this.energySlot.deserializeNBT(nbt.getCompound("EnergySlots"));
        if (nbt.contains("InputSlots"))
            this.inputSlot.deserializeNBT(nbt.getCompound("InputSlots"));
        if (nbt.contains("OutputSlots"))
            this.outputSlots.deserializeNBT(nbt.getCompound("OutputSlots"));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.put("EnergySlots", this.energySlot.serializeNBT());
        nbt.put("InputSlots", this.inputSlot.serializeNBT());
        nbt.put("OutputSlots", this.outputSlots.serializeNBT());
        return nbt;
    }

    private LazyOptional<IItemHandlerModifiable> combinedInvLazyOptional = LazyOptional.of(() -> combinedHandler);
    private LazyOptional<IItemHandlerModifiable> inputLazyOptional = LazyOptional.of(() -> inputSlot);
    private LazyOptional<IItemHandlerModifiable> outputLazyOptional = LazyOptional.of(() -> outputSlots);
    private LazyOptional<IItemHandlerModifiable> energySlotLazyOptional = LazyOptional.of(() -> energySlot);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null)
                return combinedInvLazyOptional.cast();
            else if (side == Direction.UP)
                return inputLazyOptional.cast();
            else if (side == Direction.DOWN)
                return outputLazyOptional.cast();
            else
                return energySlotLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
