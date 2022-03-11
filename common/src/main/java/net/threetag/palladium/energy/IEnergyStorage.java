package net.threetag.palladium.energy;

public interface IEnergyStorage {

    default boolean canInsert() {
        return true;
    }

    int insertEnergy(int maxAmount, boolean simulate);

    default boolean canExtract() {
        return true;
    }

    int extractEnergy(int maxAmount, boolean simulate);

    int getEnergyAmount();

    int getEnergyCapacity();

}
