package net.threetag.palladium.energy;

public interface IEnergyStorage {

    default boolean canInsert() {
        return true;
    }

    int insertEnergy(int maxAmount, boolean simulate);

    default boolean canWithdraw() {
        return true;
    }

    int withdrawEnergy(int maxAmount, boolean simulate);

    int getEnergyAmount();

    int getEnergyCapacity();

}
