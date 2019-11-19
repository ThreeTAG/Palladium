package net.threetag.threecore.base.tileentity;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.base.block.StirlingGeneratorBlock;
import net.threetag.threecore.base.block.TCBaseBlocks;
import net.threetag.threecore.base.inventory.StirlingGeneratorContainer;
import net.threetag.threecore.util.energy.IEnergyConfig;
import net.threetag.threecore.util.energy.IEnergyStorageModifiable;
import net.threetag.threecore.util.energy.NoReceiveEnergyWrapper;
import net.threetag.threecore.util.fluid.FluidTankExt;
import net.threetag.threecore.util.fluid.TCFluidUtil;
import net.threetag.threecore.util.item.ItemStackHandlerExt;
import net.threetag.threecore.util.player.PlayerHelper;

import javax.annotation.Nullable;

public class StirlingGeneratorTileEntity extends MachineTileEntity {

    public static final int TANK_CAPACITY = 5000;

    protected final IIntArray intArray = new IIntArray() {
        @Override
        public int get(int i) {
            switch (i) {
                case 0:
                    return burnTime;
                case 1:
                    return maxBurnTime;
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
                    burnTime = value;
                case 1:
                    maxBurnTime = value;
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

    public FluidTank fluidTank = new FluidTankExt(TANK_CAPACITY).setCallback(f -> {
        if (getWorld() != null)
            getWorld().notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
    }).setSoundHandler(sound -> {
        if (sound != null)
            PlayerHelper.playSoundToAll(world, getPos().getX(), getPos().getY(), getPos().getZ(), 50, sound, SoundCategory.BLOCKS);
    }).setValidator((fluidStack) -> fluidStack.getFluid().isIn(FluidTags.WATER));

    private ItemStackHandlerExt fuelSlot = new ItemStackHandlerExt(1)
            .setValidator((handler, slot, stack) -> AbstractFurnaceTileEntity.isFuel(stack))
            .setChangedCallback((handler, slot) -> StirlingGeneratorTileEntity.this.markDirty());

    private ItemStackHandlerExt fluidSlots = new ItemStackHandlerExt(4) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    }.setValidator((handler, slot, stack) -> FluidUtil.getFluidHandler(stack).isPresent())
            .setChangedCallback((handler, slot) -> {
                StirlingGeneratorTileEntity.this.markDirty();
                FluidTank tank = fluidTank;
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

    protected int burnTime;
    protected int maxBurnTime;
    private CombinedInvWrapper combinedHandler = new CombinedInvWrapper(fuelSlot, fluidSlots);

    public StirlingGeneratorTileEntity() {
        super(TCBaseBlocks.STIRLING_GENERATOR_TILE_ENTITY);
    }

    @Override
    protected IEnergyStorageModifiable createEnergyStorage(int energy) {
        return new NoReceiveEnergyWrapper(super.createEnergyStorage(energy));
    }

    @Override
    public IEnergyConfig getEnergyConfig() {
        return ThreeCoreServerConfig.ENERGY.STIRLING_GENERATOR;
    }

    @Override
    public void tick() {
        boolean currentLit = this.burnTime > 0;
        ItemStack fuel = this.fuelSlot.getStackInSlot(0);

        if (this.burnTime <= 0 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored()
                && !fuel.isEmpty() && AbstractFurnaceTileEntity.isFuel(fuel) & !this.fluidTank.getFluid().isEmpty()
                && this.fluidTank.getFluid().getFluid().isIn(FluidTags.WATER) && !this.fluidTank.drain(100, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
            this.maxBurnTime = ForgeHooks.getBurnTime(fuel);
            this.burnTime = maxBurnTime;
            this.fluidTank.drain(100, IFluidHandler.FluidAction.EXECUTE);
            if (fuel.hasContainerItem()) {
                this.fuelSlot.setStackInSlot(0, fuel.getContainerItem());
            } else {
                fuel.shrink(1);
            }
        }

        if (this.burnTime > 0) {
            if (this.burnTime % 900 == 0) {
                if (!this.fluidTank.drain(100, IFluidHandler.FluidAction.SIMULATE).isEmpty()) {
                    this.fluidTank.drain(100, IFluidHandler.FluidAction.EXECUTE);
                    this.burnTime--;
                    this.energyStorage.receiveEnergy(10, false);
                }
            } else {
                this.burnTime--;
                this.energyStorage.modifyEnergy(10);
            }
        }

        if (this.energyStorage.getEnergyStored() > 0) {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.UP) {
                    TileEntity tileEntity = this.world.getTileEntity(this.getPos().offset(direction));

                    if (tileEntity != null && !(tileEntity instanceof StirlingGeneratorTileEntity)) {
                        tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(energyStorage -> {
                            if (energyStorage.canReceive()) {
                                this.energyStorage.modifyEnergy(-energyStorage.receiveEnergy(Math.min(this.energyStorage.getEnergyStored(), this.energyStorage.getMaxExtract()), false));
                            }
                        });
                    }
                }
            }
        }

        boolean lit = this.burnTime > 0;
        if (currentLit != lit) {
            this.markDirty();
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(StirlingGeneratorBlock.LIT, lit), 3);
        }
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.threecore.stirling_generator");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new StirlingGeneratorContainer(id, player, this, this.intArray);
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.fluidTank.readFromNBT(tag.getCompound("FluidTank"));
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbt = super.getUpdateTag();
        nbt.put("FluidTank", this.fluidTank.writeToNBT(new CompoundNBT()));
        return nbt;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.handleUpdateTag(pkt.getNbtCompound());
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        if (nbt.contains("FuelSlots"))
            this.fuelSlot.deserializeNBT(nbt.getCompound("FuelSlots"));
        if (nbt.contains("FluidSlots"))
            this.fluidSlots.deserializeNBT(nbt.getCompound("FluidSlots"));

        this.fluidTank.readFromNBT(nbt.getCompound("FluidTank"));
        this.burnTime = nbt.getInt("BurnTime");
        this.maxBurnTime = nbt.getInt("MaxBurnTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.put("FuelSlots", this.fuelSlot.serializeNBT());
        nbt.put("FluidSlots", this.fluidSlots.serializeNBT());
        nbt.put("FluidTank", this.fluidTank.writeToNBT(new CompoundNBT()));
        nbt.putInt("BurnTime", this.burnTime);
        nbt.putInt("MaxBurnTime", this.maxBurnTime);
        return super.write(nbt);
    }

    private LazyOptional<IItemHandlerModifiable> combinedInvLazyOptional = LazyOptional.of(() -> combinedHandler);
    private LazyOptional<IItemHandlerModifiable> fuelSlotLazyOptional = LazyOptional.of(() -> fuelSlot);
    private LazyOptional<IItemHandlerModifiable> inputFluidSlotLazyOptional = LazyOptional.of(() -> new RangedWrapper(fluidSlots, 0, 1));
    private LazyOptional<IItemHandlerModifiable> outputFluidSlotLazyOptional = LazyOptional.of(() -> new RangedWrapper(fluidSlots, 1, 2));
    private LazyOptional<IFluidHandler> fluidHandlerLazyOptional = LazyOptional.of(() -> fluidTank);

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null)
                return combinedInvLazyOptional.cast();
            else if (side == Direction.UP)
                return fuelSlotLazyOptional.cast();
            else if (side == Direction.DOWN)
                return outputFluidSlotLazyOptional.cast();
            else
                return inputFluidSlotLazyOptional.cast();
        } else if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidHandlerLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
