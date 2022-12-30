package net.threetag.palladium.energy.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.threetag.palladium.item.EnergyItem;

public class ItemEnergyStorage implements IEnergyStorage {

    protected final ItemStack stack;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public ItemEnergyStorage(ItemStack stack, EnergyItem energyItem) {
        this(stack, energyItem.getEnergyCapacity(stack), energyItem.getEnergyMaxInput(stack), energyItem.getEnergyMaxOutput(stack));
    }

    public ItemEnergyStorage(ItemStack stack, int capacity) {
        this(stack, capacity, capacity, capacity);
    }

    public ItemEnergyStorage(ItemStack stack, int capacity, int maxTransfer) {
        this(stack, capacity, maxTransfer, maxTransfer);
    }

    public ItemEnergyStorage(ItemStack stack, int capacity, int maxReceive, int maxExtract) {
        this.stack = stack;
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive()) {
            return 0;
        } else {
            int energyReceived = Math.min(this.capacity - this.getEnergyStored(), Math.min(this.maxReceive, maxReceive));
            if (!simulate) {
                this.setEnergyStored(this.getEnergyStored() + energyReceived);
            }

            return energyReceived;
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) {
            return 0;
        } else {
            int energyExtracted = Math.min(this.getEnergyStored(), Math.min(this.maxExtract, maxExtract));
            if (!simulate) {
                this.setEnergyStored(this.getEnergyStored() - energyExtracted);
            }

            return energyExtracted;
        }
    }

    @Override
    public int getEnergyStored() {
        return this.stack.getOrCreateTag().getInt("energy");
    }

    public ItemEnergyStorage setEnergyStored(int energy) {
        this.stack.getOrCreateTag().putInt("energy", energy);
        return this;
    }

    @Override
    public int getMaxEnergyStored() {
        return this.capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }

}
