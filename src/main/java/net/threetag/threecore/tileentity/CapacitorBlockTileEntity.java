package net.threetag.threecore.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.threetag.threecore.block.CapacitorBlock;
import net.threetag.threecore.container.CapacitorBlockContainer;
import net.threetag.threecore.util.energy.EnergyStorageExt;
import net.threetag.threecore.util.energy.IEnergyConfig;
import net.threetag.threecore.util.energy.NoReceiveEnergyWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class CapacitorBlockTileEntity extends LockableItemCapTileEntity implements ITickableTileEntity {

    public CapacitorBlock.Type type;
    public EnergyStorageExt energyStorage;
    public final ItemStackHandler inputSlot = new ItemStackHandler(1);
    public final ItemStackHandler outputSlot = new ItemStackHandler(1);
    public final CombinedInvWrapper combinedInvWrapper = new CombinedInvWrapper(inputSlot, outputSlot);

    protected final IIntArray intArray = new IIntArray() {
        @Override
        public int get(int i) {
            switch (i) {
                case 0:
                    return energyStorage.getEnergyStored();
                case 1:
                    return energyStorage.getMaxEnergyStored();
                default:
                    return 0;
            }
        }

        @Override
        public void set(int i, int value) {
            switch (i) {
                case 0:
                    energyStorage.setEnergyStored(value);
                case 1:
                    energyStorage.setMaxEnergyStored(value);
            }
        }

        @Override
        public int size() {
            return 2;
        }
    };

    public CapacitorBlockTileEntity(CapacitorBlock.Type type) {
        super(TCTileEntityTypes.CAPACITOR_BLOCK.get());
        this.type = type;
        this.energyStorage = new EnergyStorageExt(getEnergyConfig());
    }

    public IEnergyConfig getEnergyConfig() {
        return this.type.getEnergyConfig();
    }

    @Override
    public void tick() {
        if (this.world != null && !this.world.isRemote) {
            this.inputSlot.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> {
                int energy = e.extractEnergy(e.getEnergyStored(), true);
                energy = this.energyStorage.receiveEnergy(energy, true);
                if (energy > 0) {
                    this.energyStorage.receiveEnergy(energy, false);
                    e.extractEnergy(energy, false);
                }
            });

            this.outputSlot.getStackInSlot(0).getCapability(CapabilityEnergy.ENERGY).ifPresent((e) -> {
                int energy = this.energyStorage.extractEnergy(this.energyStorage.getEnergyStored(), true);
                energy = e.receiveEnergy(energy, true);
                if (energy > 0) {
                    e.receiveEnergy(energy, false);
                    this.energyStorage.extractEnergy(energy, false);
                }
            });

            TileEntity tileEntity = this.world.getTileEntity(this.getPos().down());

            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY, Direction.UP).ifPresent(energyStorage -> {
                    if (energyStorage.canReceive()) {
                        this.energyStorage.modifyEnergy(-energyStorage.receiveEnergy(Math.min(this.energyStorage.getEnergyStored(), this.energyStorage.getMaxExtract()), false));
                    }
                });
            }

            int level = (int) (10F * (float) this.energyStorage.getEnergyStored() / (float) this.energyStorage.getMaxEnergyStored());
            int currentLevel = this.world.getBlockState(this.pos).get(CapacitorBlock.LEVEL_0_10);

            if (level != currentLevel)
                this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(CapacitorBlock.LEVEL_0_10, level), 3);
        }
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("container.threecore.capacitor_block");
    }

    @Override
    protected Container createMenu(int id, PlayerInventory playerInventory) {
        return new CapacitorBlockContainer(id, playerInventory, this, this.intArray);
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (Objects.requireNonNull(this.world).getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void read(BlockState blockState, CompoundNBT compound) {
        super.read(blockState, compound);
        this.type = CapacitorBlock.Type.getByName(compound.getString("Type"));
        this.energyStorage = new EnergyStorageExt(getEnergyConfig(), compound.getInt("Energy"));
        this.inputSlot.deserializeNBT(compound.getCompound("InputSlot"));
        this.outputSlot.deserializeNBT(compound.getCompound("OutputSlot"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putString("Type", this.type.getString());
        compound.putInt("Energy", this.energyStorage.getEnergyStored());
        compound.put("InputSlot", this.inputSlot.serializeNBT());
        compound.put("OutputSlot", this.outputSlot.serializeNBT());
        return super.write(compound);
    }

    public final LazyOptional<IEnergyStorage> energyStorageOptional = LazyOptional.of(() -> energyStorage);
    public final NoReceiveEnergyWrapper noReceiveEnergyWrapper = new NoReceiveEnergyWrapper(this.energyStorage);
    public final LazyOptional<IEnergyStorage> energyStorageOptionalBottom = LazyOptional.of(() -> noReceiveEnergyWrapper);
    public final LazyOptional<IItemHandler> inputSlotOptional = LazyOptional.of(() -> inputSlot);
    public final LazyOptional<IItemHandler> outputSlotOptional = LazyOptional.of(() -> outputSlot);
    public final LazyOptional<IItemHandler> combinedInvOptional = LazyOptional.of(() -> combinedInvWrapper);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return side == null ? combinedInvOptional.cast() : side == Direction.DOWN ? this.outputSlotOptional.cast() : this.inputSlotOptional.cast();
        } else if (cap == CapabilityEnergy.ENERGY) {
            return side == Direction.DOWN ? energyStorageOptionalBottom.cast() : this.energyStorageOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
