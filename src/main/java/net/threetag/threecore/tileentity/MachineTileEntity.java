package net.threetag.threecore.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.threetag.threecore.util.energy.EnergyStorageExt;
import net.threetag.threecore.util.energy.IEnergyConfig;
import net.threetag.threecore.util.energy.IEnergyStorageModifiable;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class MachineTileEntity extends LockableItemCapTileEntity implements ITickableTileEntity {

    protected IEnergyStorageModifiable energyStorage;
    private LazyOptional<IEnergyStorage> energyStorageLazyOptional = LazyOptional.of(() -> energyStorage);

    public MachineTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
        this.energyStorage = createEnergyStorage(0);
    }

    protected IEnergyStorageModifiable createEnergyStorage(int energy) {
        return new EnergyStorageExt(this.getEnergyConfig(), energy);
    }

    public abstract IEnergyConfig getEnergyConfig();

    public IItemHandler getEnergyInputSlots() {
        return null;
    }

    public IItemHandler getEnergyOutputSlots() {
        return null;
    }

    @Override
    public void tick() {
        if (this.getWorld() != null && !this.getWorld().isRemote) {
            if (getEnergyInputSlots() != null) {
                for (int i = 0; i < this.getEnergyInputSlots().getSlots(); i++) {
                    this.getEnergyInputSlots().getStackInSlot(i).getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                        int energy = energyStorage.extractEnergy(energyStorage.getEnergyStored(), true);
                        energy = this.energyStorage.receiveEnergy(energy, true);
                        if (energy > 0) {
                            this.energyStorage.receiveEnergy(energy, false);
                            energyStorage.extractEnergy(energy, false);
                        }
                    });
                }
            }

            if (getEnergyOutputSlots() != null) {
                for (int i = 0; i < this.getEnergyOutputSlots().getSlots(); i++) {
                    this.getEnergyOutputSlots().getStackInSlot(i).getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                        int energy = this.energyStorage.extractEnergy(this.energyStorage.getEnergyStored(), true);
                        energy = energyStorage.receiveEnergy(energy, true);
                        if (energy > 0) {
                            energyStorage.receiveEnergy(energy, false);
                            this.energyStorage.extractEnergy(energy, false);
                        }
                    });
                }
            }
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (Objects.requireNonNull(this.world).getTileEntity(this.pos) != this) {
            return false;
        } else {
            return !(player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) > 64.0D);
        }
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY)
            return this.energyStorageLazyOptional.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        this.energyStorage = createEnergyStorage(nbt.getInt("Energy"));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("Energy", this.energyStorage.getEnergyStored());
        return super.write(nbt);
    }
}
