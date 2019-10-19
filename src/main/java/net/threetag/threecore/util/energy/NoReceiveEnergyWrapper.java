package net.threetag.threecore.util.energy;

import net.minecraftforge.energy.IEnergyStorage;

public class NoReceiveEnergyWrapper implements IEnergyStorage {

    private final IEnergyStorage energyStorage;

    public NoReceiveEnergyWrapper(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return this.energyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return this.energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return this.energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return this.energyStorage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return false;
    }
}
