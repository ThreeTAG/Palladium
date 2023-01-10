package net.threetag.palladium.energy.fabric;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;

import java.util.Optional;

public class EnergyHelperImpl {

    public static Optional<IEnergyStorage> getFromItemStack(ItemStack stack) {
        var storage = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
        return storage == null ? Optional.empty() : Optional.of(new Wrapper(storage));
    }

    public static Optional<IEnergyStorage> getFromBlockEntity(Level level, BlockPos pos, @Nullable Direction side) {
        EnergyStorage energyStorage = EnergyStorage.SIDED.find(level, pos, side);
        return Optional.ofNullable(energyStorage == null ? null : new Wrapper(energyStorage));
    }

    public static long moveBetweenBlockEntities(Level level, BlockPos from, Direction fromSide, BlockPos to, Direction toSide, long maxAmount) {
        EnergyStorage fromStorage = EnergyStorage.SIDED.find(level, from, fromSide);
        EnergyStorage toStorage = EnergyStorage.SIDED.find(level, to, toSide);
        return EnergyStorageUtil.move(fromStorage, toStorage, maxAmount, null);
    }

    public record Wrapper(EnergyStorage fabricStorage) implements IEnergyStorage {

        @Override
        public boolean canInsert() {
            return this.fabricStorage.supportsInsertion();
        }

        @Override
        public int insertEnergy(int maxAmount, boolean simulate) {
            return (int) this.fabricStorage.insert(maxAmount, Transaction.openNested(null));
        }

        @Override
        public boolean canWithdraw() {
            return this.fabricStorage.supportsExtraction();
        }

        @Override
        public int withdrawEnergy(int maxAmount, boolean simulate) {
            return (int) this.fabricStorage.extract(maxAmount, Transaction.openNested(null));
        }

        @Override
        public int getEnergyAmount() {
            return (int) this.fabricStorage.getAmount();
        }

        @Override
        public int getEnergyCapacity() {
            return (int) this.fabricStorage.getCapacity();
        }
    }

}
