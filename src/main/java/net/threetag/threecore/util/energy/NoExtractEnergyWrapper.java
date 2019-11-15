package net.threetag.threecore.util.energy;

import net.minecraftforge.energy.IEnergyStorage;

public class NoExtractEnergyWrapper implements IEnergyStorageModifiable {

    private final IEnergyStorage energyStorage;

    public NoExtractEnergyWrapper(IEnergyStorage energyStorage) {
        this.energyStorage = energyStorage;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return this.energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
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
        return false;
    }

    @Override
    public boolean canReceive() {
        return this.energyStorage.canReceive();
    }

    @Override
    public void setEnergyStored(int energy) {
        if (this.energyStorage instanceof IEnergyStorageModifiable)
            ((IEnergyStorageModifiable) this.energyStorage).setEnergyStored(energy);
    }

    @Override
    public void setMaxEnergyStored(int energy) {
        if (this.energyStorage instanceof IEnergyStorageModifiable)
            ((IEnergyStorageModifiable) this.energyStorage).setMaxEnergyStored(energy);
    }

    @Override
    public void modifyEnergy(int amount) {
        if (this.energyStorage instanceof IEnergyStorageModifiable)
            ((IEnergyStorageModifiable) this.energyStorage).modifyEnergy(amount);
    }

    @Override
    public int getMaxExtract() {
        if (this.energyStorage instanceof IEnergyStorageModifiable)
            return ((IEnergyStorageModifiable) this.energyStorage).getMaxExtract();
        return 0;
    }

    @Override
    public int getMaxReceive() {
        if (this.energyStorage instanceof IEnergyStorageModifiable)
            return ((IEnergyStorageModifiable) this.energyStorage).getMaxReceive();
        return 0;
    }
}
