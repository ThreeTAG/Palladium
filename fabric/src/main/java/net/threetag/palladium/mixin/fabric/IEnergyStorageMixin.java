package net.threetag.palladium.mixin.fabric;

import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.threetag.palladium.energy.IEnergyStorage;
import org.spongepowered.asm.mixin.Mixin;
import team.reborn.energy.api.EnergyStorage;

@Mixin(IEnergyStorage.class)
public interface IEnergyStorageMixin extends EnergyStorage {

    default IEnergyStorage cast() {
        return (IEnergyStorage) this;
    }

    @Override
    default boolean supportsInsertion() {
        return this.cast().canInsert();
    }

    @Override
    default long insert(long maxAmount, TransactionContext transaction) {
        return this.cast().insertEnergy((int) maxAmount, false);
    }

    @Override
    default boolean supportsExtraction() {
        return this.cast().canExtract();
    }

    @Override
    default long extract(long maxAmount, TransactionContext transaction) {
        return this.cast().extractEnergy((int) maxAmount, false);
    }

    @Override
    default long getAmount() {
        return this.cast().getEnergyAmount();
    }

    @Override
    default long getCapacity() {
        return this.cast().getEnergyCapacity();
    }
}
