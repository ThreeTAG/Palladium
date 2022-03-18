package net.threetag.palladium.mixin.forge;

import net.threetag.palladium.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IEnergyStorage.class)
public interface IEnergyStorageMixin extends net.minecraftforge.energy.IEnergyStorage {

    default IEnergyStorage cast() {
        return (IEnergyStorage) this;
    }

    @Override
    default int receiveEnergy(int maxReceive, boolean simulate) {
        return this.cast().insertEnergy(maxReceive, simulate);
    }

    @Override
    default int extractEnergy(int maxExtract, boolean simulate) {
        return this.cast().withdrawEnergy(maxExtract, simulate);
    }

    @Override
    default int getEnergyStored() {
        return this.cast().getEnergyAmount();
    }

    @Override
    default int getMaxEnergyStored() {
        return this.cast().getEnergyCapacity();
    }

    @Override
    default boolean canExtract() {
        return this.cast().canWithdraw();
    }

    @Override
    default boolean canReceive() {
        return this.cast().canInsert();
    }
}
