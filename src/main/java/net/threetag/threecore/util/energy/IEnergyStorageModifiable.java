package net.threetag.threecore.util.energy;

import net.minecraftforge.energy.IEnergyStorage;

public interface IEnergyStorageModifiable extends IEnergyStorage {

    void setEnergyStored(int energy);

    void setMaxEnergyStored(int energy);

    void modifyEnergy(int amount);

    int getMaxExtract();

    int getMaxReceive();

}
