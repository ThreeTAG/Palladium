package net.threetag.threecore.base.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
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
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.base.block.CapacitorBlock;
import net.threetag.threecore.base.inventory.CapacitorBlockContainer;
import net.threetag.threecore.base.inventory.GrinderContainer;
import net.threetag.threecore.util.energy.EnergyStorageExt;
import net.threetag.threecore.util.tileentity.LockableItemCapTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class CapacitorBlockTileEntity extends LockableItemCapTileEntity implements ITickableTileEntity {

    // TODO make those energy values a config
    public EnergyStorageExt energyStorage = new EnergyStorageExt(40000, 1000);
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

    public CapacitorBlockTileEntity() {
        super(ThreeCoreBase.CAPACITOR_BLOCK_TILE_ENTITY);
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
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.energyStorage = new EnergyStorageExt(40000, 1000, 1000, compound.getInt("Energy"));
        this.inputSlot.deserializeNBT(compound.getCompound("InputSlot"));
        this.outputSlot.deserializeNBT(compound.getCompound("OutputSlot"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Energy", this.energyStorage.getEnergyStored());
        compound.put("InputSlot", this.inputSlot.serializeNBT());
        compound.put("OutputSlot", this.outputSlot.serializeNBT());
        return super.write(compound);
    }

    public final LazyOptional<IEnergyStorage> energyStorageOptional = LazyOptional.of(() -> energyStorage);
    public final LazyOptional<IItemHandler> inputSlotOptional = LazyOptional.of(() -> inputSlot);
    public final LazyOptional<IItemHandler> outputSlotOptional = LazyOptional.of(() -> outputSlot);
    public final LazyOptional<IItemHandler> combinedInvOptional = LazyOptional.of(() -> combinedInvWrapper);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return side == null ? combinedInvOptional.cast() : side == Direction.DOWN ? this.outputSlotOptional.cast() : this.inputSlotOptional.cast();
        } else if (cap == CapabilityEnergy.ENERGY) {
            return this.energyStorageOptional.cast();
        }
        return super.getCapability(cap, side);
    }
}
