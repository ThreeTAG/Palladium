package net.threetag.threecore.util.energy;

import net.minecraft.util.math.MathHelper;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageExt extends EnergyStorage implements IEnergyStorageModifiable {

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

    public EnergyStorageExt(IEnergyConfig energyConfig) {
        this(energyConfig.getCapacity(), energyConfig.getPower());
    }

    public EnergyStorageExt(IEnergyConfig energyConfig, int energy) {
        this(energyConfig.getCapacity(), energyConfig.getPower(), energyConfig.getPower(), energy);
    }

    @Override
    public void setEnergyStored(int energy) {
        this.energy = energy;
    }

    @Override
    public void setMaxEnergyStored(int energy) {
        this.capacity = energy;
    }

    @Override
    public void modifyEnergy(int amount) {
        this.energy = MathHelper.clamp(this.energy + amount, 0, this.capacity);
    }

    @Override
    public int getMaxExtract() {
        return maxExtract;
    }

    @Override
    public int getMaxReceive() {
        return maxReceive;
    }

    public static EnergyStorageExt noReceive(IEnergyConfig energyConfig) {
        return noReceive(energyConfig, 0);
    }

    public static EnergyStorageExt noReceive(IEnergyConfig energyConfig, int energy) {
        return new EnergyStorageExt(energyConfig.getCapacity(), 0, energyConfig.getPower(), energy);
    }

    public static EnergyStorageExt noExtract(IEnergyConfig energyConfig) {
        return noExtract(energyConfig, 0);
    }

    public static EnergyStorageExt noExtract(IEnergyConfig energyConfig, int energy) {
        return new EnergyStorageExt(energyConfig.getCapacity(), energyConfig.getPower(), 0, energy);
    }

}
