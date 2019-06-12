package com.threetag.threecore.util.energy;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageExt extends EnergyStorage {

    public EnergyStorageExt(int capacity) {
        super(capacity);
    }

    public EnergyStorageExt(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public EnergyStorageExt(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public EnergyStorageExt(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    public void setMaxEnergyStored(int energy) {
        this.capacity = energy;
    }

}
