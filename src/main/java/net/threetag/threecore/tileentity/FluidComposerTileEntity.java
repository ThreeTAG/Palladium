package net.threetag.threecore.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.container.FluidComposerContainer;
import net.threetag.threecore.fluid.FluidTankExt;
import net.threetag.threecore.item.FluidInventory;
import net.threetag.threecore.item.ItemStackHandlerExt;
import net.threetag.threecore.item.recipe.FluidComposingRecipe;
import net.threetag.threecore.util.PlayerUtil;
import net.threetag.threecore.util.TCFluidUtil;
import net.threetag.threecore.util.energy.IEnergyConfig;

import javax.annotation.Nullable;

public class FluidComposerTileEntity extends ProgressableMachineTileEntity<FluidComposingRecipe> {

    public static final int TANK_CAPACITY = 5000;

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

    public FluidTankExt inputFluidTank = new FluidTankExt(TANK_CAPACITY).setCallback(f -> {
        this.updateRecipe(this.recipeWrapper);
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
    }).setSoundHandler(sound -> {
        if (sound != null)
            PlayerUtil.playSoundToAll(world, getPos().getX(), getPos().getY(), getPos().getZ(), 50, sound, SoundCategory.BLOCKS);
    });

    public FluidTankExt outputFluidTank = new FluidTankExt(TANK_CAPACITY).setCallback(f -> {
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
    }).setSoundHandler(sound -> {
        if (sound != null)
            PlayerUtil.playSoundToAll(world, getPos().getX(), getPos().getY(), getPos().getZ(), 50, sound, SoundCategory.BLOCKS);
    });

    private ItemStackHandlerExt energySlot = new ItemStackHandlerExt(1)
            .setValidator((handler, slot, stack) -> stack.getCapability(CapabilityEnergy.ENERGY).isPresent())
            .setChangedCallback((handler, slot) -> FluidComposerTileEntity.this.markDirty());

    private ItemStackHandlerExt inputSlots = new ItemStackHandlerExt(9)
            .setChangedCallback((handler, slot) -> {
                this.updateRecipe(this.recipeWrapper);
                FluidComposerTileEntity.this.markDirty();
            });

    private ItemStackHandlerExt fluidSlots = new ItemStackHandlerExt(4) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }.setValidator((handler, slot, stack) -> FluidUtil.getFluidHandler(stack).isPresent())
            .setChangedCallback((handler, slot) -> {
                FluidComposerTileEntity.this.markDirty();
                FluidTank tank = slot <= 1 ? inputFluidTank : outputFluidTank;
                if (slot == 0 || slot == 2) {
                    FluidActionResult res = TCFluidUtil.transferFluidFromItemToTank(handler.getStackInSlot(slot), tank, handler, null);
                    if (res.isSuccess()) {
                        handler.setStackInSlot(slot, res.getResult());
                    }
                } else {
                    FluidActionResult res = TCFluidUtil.transferFluidFromTankToItem(handler.getStackInSlot(slot), tank, handler, null);
                    if (res.isSuccess()) {
                        handler.setStackInSlot(slot, res.getResult());
                    }
                }
            });

    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(energySlot, fluidSlots, inputSlots);
    public FluidInventory recipeWrapper = new FluidInventory(this.inputSlots, inputFluidTank);

    public FluidComposerTileEntity() {
        super(TCTileEntityTypes.FLUID_COMPOSER.get());
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.threecore.fluid_composer");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new FluidComposerContainer(id, player, this, this.intArray);
    }

    @Override
    public IRecipeType<FluidComposingRecipe> getRecipeType() {
        return FluidComposingRecipe.RECIPE_TYPE;
    }

    @Override
    public IEnergyConfig getEnergyConfig() {
        return ThreeCoreServerConfig.ENERGY.FLUID_COMPOSER;
    }

    @Override
    public IItemHandler getEnergyInputSlots() {
        return this.energySlot;
    }

    @Override
    public boolean canWork(FluidComposingRecipe recipe) {
        FluidStack result = recipe.getResult(this.recipeWrapper);

        if (result.isEmpty())
            return false;

        return this.outputFluidTank.fill(result, IFluidHandler.FluidAction.SIMULATE) == result.getAmount();
    }

    public void produceOutput(FluidComposingRecipe recipe) {
        if (!TCFluidUtil.drainIngredient(this.inputFluidTank, recipe.getInputFluid(), IFluidHandler.FluidAction.EXECUTE))
            return;

        this.outputFluidTank.fill(recipe.getResult(this.recipeWrapper), IFluidHandler.FluidAction.EXECUTE);

        for (int i = 0; i < this.inputSlots.getSlots(); i++) {
            this.inputSlots.getStackInSlot(i).shrink(1);
        }

        this.updateRecipe(this.recipeWrapper);
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.inputFluidTank.readFromNBT(tag.getCompound("InputFluid"));
        this.outputFluidTank.readFromNBT(tag.getCompound("OutputFluid"));
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.put("InputFluid", this.inputFluidTank.writeToNBT(new CompoundNBT()));
        nbt.put("OutputFluid", this.outputFluidTank.writeToNBT(new CompoundNBT()));
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        if (nbt.contains("EnergySlots"))
            this.energySlot.deserializeNBT(nbt.getCompound("EnergySlots"));
        if (nbt.contains("InputSlots"))
            this.inputSlots.deserializeNBT(nbt.getCompound("InputSlots"));
        if (nbt.contains("FluidSlots"))
            this.fluidSlots.deserializeNBT(nbt.getCompound("FluidSlots"));

        this.inputFluidTank.readFromNBT(nbt.getCompound("InputFluid"));
        this.outputFluidTank.readFromNBT(nbt.getCompound("OutputFluid"));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.put("EnergySlots", this.energySlot.serializeNBT());
        nbt.put("InputSlots", this.inputSlots.serializeNBT());
        nbt.put("FluidSlots", this.fluidSlots.serializeNBT());
        nbt.put("InputFluid", this.inputFluidTank.writeToNBT(new CompoundNBT()));
        nbt.put("OutputFluid", this.outputFluidTank.writeToNBT(new CompoundNBT()));
        return super.write(nbt);
    }

    private LazyOptional<IItemHandlerModifiable> combinedInvLazyOptional = LazyOptional.of(() -> combinedHandler);
    private LazyOptional<IItemHandlerModifiable> energySlotLazyOptional = LazyOptional.of(() -> energySlot);
    private LazyOptional<IItemHandlerModifiable> fluidSlotLazyOptional = LazyOptional.of(() -> fluidSlots);
    private LazyOptional<IItemHandlerModifiable> inputLazyOptional = LazyOptional.of(() -> inputSlots);
    private LazyOptional<IFluidHandler> inputFluidHandler = LazyOptional.of(() -> inputFluidTank);
    private LazyOptional<IFluidHandler> outputFluidHandler = LazyOptional.of(() -> outputFluidTank);

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null)
                return combinedInvLazyOptional.cast();
            else if (side == Direction.UP)
                return inputLazyOptional.cast();
            else if (side == Direction.DOWN)
                return fluidSlotLazyOptional.cast();
            else
                return energySlotLazyOptional.cast();
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (side == Direction.UP || side == Direction.DOWN)
                return outputFluidHandler.cast();
            else
                return inputFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
