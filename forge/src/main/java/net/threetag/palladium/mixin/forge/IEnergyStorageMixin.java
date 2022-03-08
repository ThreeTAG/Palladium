package net.threetag.palladium.mixin.forge;

import net.threetag.palladium.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IEnergyStorage.class)
public class IEnergyStorageMixin implements net.minecraftforge.energy.IEnergyStorage {

    public IEnergyStorage cast() {
        return (IEnergyStorage) this;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.cast().insertEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.cast().extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return this.cast().getEnergyAmount();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.cast().getEnergyCapacity();
    }

    @Override
    public boolean canExtract() {
        return this.cast().canExtract();
    }

    @Override
    public boolean canReceive() {
        return this.cast().canInsert();
    }
}
