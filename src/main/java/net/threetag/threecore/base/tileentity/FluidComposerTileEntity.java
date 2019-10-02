package net.threetag.threecore.base.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.base.block.MachineBlock;
import net.threetag.threecore.base.inventory.FluidComposerContainer;
import net.threetag.threecore.base.recipe.FluidComposingRecipe;
import net.threetag.threecore.util.energy.EnergyStorageExt;
import net.threetag.threecore.util.fluid.FluidInventory;
import net.threetag.threecore.util.fluid.FluidTankExt;
import net.threetag.threecore.util.fluid.TCFluidUtil;
import net.threetag.threecore.util.player.PlayerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class FluidComposerTileEntity extends MachineTileEntity {

    public static final int TANK_CAPACITY = 5000;

    private EnergyStorageExt energyStorage = new EnergyStorageExt(ThreeCoreServerConfig.ENERGY.FLUID_COMPOSER);
    public int progress;
    public int progressMax;
    private final Map<ResourceLocation, Integer> field_214022_n = Maps.newHashMap();

    protected final IIntArray intArray = new IIntArray() {
        @Override
        public int get(int i) {
            switch (i) {
                case 0:
                    return progress;
                case 1:
                    return progressMax;
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
                    progressMax = value;
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

    private ItemStackHandler energySlot = new ItemStackHandler(1) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
        }

        @Override
        protected void onContentsChanged(int slot) {
            FluidComposerTileEntity.this.markDirty();
        }
    };
    private ItemStackHandler inputSlots = new ItemStackHandler(9) {
        @Override
        protected void onContentsChanged(int slot) {
            FluidComposerTileEntity.this.markDirty();
        }
    };
    private ItemStackHandler fluidSlots = new ItemStackHandler(4) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return FluidUtil.getFluidHandler(stack).isPresent();
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            FluidComposerTileEntity.this.markDirty();
            FluidTank tank = slot <= 1 ? inputFluidTank : outputFluidTank;
            if (slot == 0 || slot == 2) {
                FluidActionResult res = TCFluidUtil.transferFluidFromItemToTank(this.getStackInSlot(slot), tank, fluidSlots, null);
                if (res.isSuccess()) {
                    this.setStackInSlot(slot, res.getResult());
                }
            } else {
                FluidActionResult res = TCFluidUtil.transferFluidFromTankToItem(this.getStackInSlot(slot), tank, fluidSlots, null);
                if (res.isSuccess()) {
                    this.setStackInSlot(slot, res.getResult());
                }
            }
        }
    };

    public FluidTankExt inputFluidTank = new FluidTankExt(TANK_CAPACITY).setCallback(f -> {
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
    }).setSoundHandler(sound -> {
        if (sound != null)
            PlayerHelper.playSoundToAll(world, getPos().getX(), getPos().getY(), getPos().getZ(), 50, sound, SoundCategory.BLOCKS);
    });

    public FluidTankExt outputFluidTank = new FluidTankExt(TANK_CAPACITY).setCallback(f -> {
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
    }).setSoundHandler(sound -> {
        if (sound != null)
            PlayerHelper.playSoundToAll(world, getPos().getX(), getPos().getY(), getPos().getZ(), 50, sound, SoundCategory.BLOCKS);
    });

    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(energySlot, fluidSlots, inputSlots);
    public FluidInventory recipeWrapper = new FluidInventory(this.inputSlots, inputFluidTank);

    public FluidComposerTileEntity() {
        super(ThreeCoreBase.FLUID_COMPOSER_TILE_ENTITY);
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
    public void tick() {
        boolean working = this.isWorking();
        boolean dirty = false;

        if (this.world != null && !this.world.isRemote) {
            this.energySlot.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> {
                int energy = e.extractEnergy(e.getEnergyStored(), true);
                energy = energyStorage.receiveEnergy(energy, true);
                if (energy > 0) {
                    this.energyStorage.receiveEnergy(energy, false);
                    e.extractEnergy(energy, false);
                }
            });

            FluidComposingRecipe recipe = this.world.getRecipeManager().getRecipe(FluidComposingRecipe.RECIPE_TYPE, this.recipeWrapper, this.world).orElse(null);
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
        }

        if (working != this.isWorking()) {
            dirty = true;
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(MachineBlock.LIT, this.isWorking()), 3);
        }

        if (dirty)
            this.markDirty();
    }

    public boolean isWorking() {
        return this.progress > 0;
    }

    public boolean canWork(FluidComposingRecipe recipe) {
        if (recipe != null && this.energyStorage.extractEnergy(1, true) == 1) {
            FluidStack result = recipe.getResult(this.recipeWrapper);

            if (result.isEmpty())
                return false;

            return this.outputFluidTank.fill(result, IFluidHandler.FluidAction.SIMULATE) == result.getAmount();
        } else {
            return false;
        }
    }

    public void produceOutput(FluidComposingRecipe recipe) {
        if (recipe != null && this.canWork(recipe)) {
            if (!TCFluidUtil.drainIngredient(this.inputFluidTank, recipe.getInputFluid(), IFluidHandler.FluidAction.EXECUTE))
                return;

            this.outputFluidTank.fill(recipe.getResult(this.recipeWrapper), IFluidHandler.FluidAction.EXECUTE);

            if (!this.world.isRemote) {
                this.canUseRecipe(this.world, null, recipe);
            }

            for (int i = 0; i < this.inputSlots.getSlots(); i++) {
                this.inputSlots.getStackInSlot(i).shrink(1);
            }
        }
    }

    @Override
    public void fillStackedContents(RecipeItemHelper recipeItemHelper) {
        for (int i = 0; i < this.combinedHandler.getSlots(); i++) {
            ItemStack stack = this.combinedHandler.getStackInSlot(i);
            recipeItemHelper.accountStack(stack);
        }
    }

    @Override
    public void setRecipeUsed(IRecipe recipe) {
        if (recipe != null) {
            this.field_214022_n.compute(recipe.getId(), (resourceLocation, integer) -> 1 + (integer == null ? 0 : integer));
        }
    }

    @Nullable
    @Override
    public IRecipe<?> getRecipeUsed() {
        return null;
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

        this.progress = nbt.getInt("Progress");
        this.progressMax = nbt.getInt("ProgressMax");
        this.energyStorage = new EnergyStorageExt(ThreeCoreServerConfig.ENERGY.FLUID_COMPOSER, nbt.getInt("Energy"));

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
        nbt.putInt("Progress", this.progress);
        nbt.putInt("ProgressMax", this.progressMax);
        nbt.putInt("Energy", this.energyStorage.getEnergyStored());
        nbt.put("EnergySlots", this.energySlot.serializeNBT());
        nbt.put("InputSlots", this.inputSlots.serializeNBT());
        nbt.put("FluidSlots", this.fluidSlots.serializeNBT());
        nbt.put("InputFluid", this.inputFluidTank.writeToNBT(new CompoundNBT()));
        nbt.put("OutputFluid", this.outputFluidTank.writeToNBT(new CompoundNBT()));
        return super.write(nbt);
    }

    private LazyOptional<IItemHandlerModifiable> combinedInvHandler = LazyOptional.of(() -> combinedHandler);
    private LazyOptional<IItemHandlerModifiable> energySlotHandler = LazyOptional.of(() -> energySlot);
    private LazyOptional<IItemHandlerModifiable> fluidSlotHandler = LazyOptional.of(() -> fluidSlots);
    private LazyOptional<IItemHandlerModifiable> inputSlotHandler = LazyOptional.of(() -> inputSlots);
    private LazyOptional<EnergyStorage> energyHandler = LazyOptional.of(() -> energyStorage);
    private LazyOptional<IFluidHandler> inputFluidHandler = LazyOptional.of(() -> inputFluidTank);
    private LazyOptional<IFluidHandler> outputFluidHandler = LazyOptional.of(() -> outputFluidTank);

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null)
                return combinedInvHandler.cast();
            else if (side == Direction.UP)
                return inputSlotHandler.cast();
            else if (side == Direction.DOWN)
                return fluidSlotHandler.cast();
            else
                return energySlotHandler.cast();
        } else if (cap == CapabilityEnergy.ENERGY)
            return energyHandler.cast();
        else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (side == Direction.UP || side == Direction.DOWN)
                return outputFluidHandler.cast();
            else
                return inputFluidHandler.cast();
        }
        return super.getCapability(cap, side);
    }
}
