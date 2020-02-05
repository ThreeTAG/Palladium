package net.threetag.threecore.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
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
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.container.HydraulicPressContainer;
import net.threetag.threecore.item.recipe.PressingRecipe;
import net.threetag.threecore.util.energy.IEnergyConfig;
import net.threetag.threecore.item.ItemStackHandlerExt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HydraulicPressTileEntity extends ProgressableMachineTileEntity<PressingRecipe> {

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
            .setChangedCallback((handler, slot) -> HydraulicPressTileEntity.this.markDirty());

    private ItemStackHandlerExt inputSlot = new ItemStackHandlerExt(2) {
        @Override
        public int getSlotLimit(int slot) {
            return slot == 0 ? 1 : super.getSlotLimit(slot);
        }
    }.setChangedCallback((handler, slot) -> {
        this.updateRecipe(this.recipeWrapper);
        HydraulicPressTileEntity.this.markDirty();
    });

    private ItemStackHandlerExt outputSlots = new ItemStackHandlerExt(1)
            .setValidator((handler, slot, stack) -> false)
            .setChangedCallback((handler, slot) -> HydraulicPressTileEntity.this.markDirty());

    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(energySlot, inputSlot, outputSlots);
    public RecipeWrapper recipeWrapper = new RecipeWrapper(this.inputSlot);

    public HydraulicPressTileEntity() {
        super(TCBlocks.HYDRAULIC_PRESS_TILE_ENTITY);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.threecore.hydraulic_press");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new HydraulicPressContainer(id, playerInventory, this, this.intArray);
    }

    @Override
    public IEnergyConfig getEnergyConfig() {
        return ThreeCoreServerConfig.ENERGY.HYDRAULIC_PRESS;
    }

    @Override
    public IRecipeType getRecipeType() {
        return PressingRecipe.RECIPE_TYPE;
    }

    @Override
    public float getXpFromRecipe(PressingRecipe recipe) {
        return recipe.getExperience();
    }

    @Override
    public IItemHandler getEnergyInputSlots() {
        return this.energySlot;
    }

    @Override
    public boolean canWork(PressingRecipe recipe) {
        ItemStack recipeOutput = recipe.getRecipeOutput();
        if (recipeOutput.isEmpty()) {
            return false;
        } else {
            boolean output;
            ItemStack outputSlot = this.outputSlots.getStackInSlot(0);

            if (outputSlot.isEmpty()) {
                output = true;
            } else if (!outputSlot.isItemEqual(recipeOutput)) {
                output = false;
            } else if (outputSlot.getCount() + recipeOutput.getCount() <= this.outputSlots.getSlotLimit(0) && outputSlot.getCount() < outputSlot.getMaxStackSize()) {
                output = true;
            } else {
                output = outputSlot.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
            }

            return output;
        }
    }

    @Override
    public void produceOutput(PressingRecipe recipe) {
        ItemStack recipeOutput = recipe.getRecipeOutput();
        ItemStack outputSlot = outputSlots.getStackInSlot(0);
        if (outputSlot.isEmpty()) {
            this.outputSlots.setStackInSlot(0, recipeOutput.copy());
        } else if (outputSlot.getItem() == recipeOutput.getItem()) {
            outputSlot.grow(recipeOutput.getCount());
        }

        this.inputSlot.getStackInSlot(1).shrink(1);
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
