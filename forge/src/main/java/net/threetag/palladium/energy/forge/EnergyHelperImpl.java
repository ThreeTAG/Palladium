package net.threetag.palladium.energy.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.threetag.palladium.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class EnergyHelperImpl {

    public static Optional<IEnergyStorage> getFromBlockEntity(Level level, BlockPos pos, @Nullable Direction side) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        AtomicReference<Optional<IEnergyStorage>> optional = new AtomicReference<>(Optional.empty());

        if (blockEntity != null) {
            blockEntity.getCapability(CapabilityEnergy.ENERGY, side).ifPresent(storage -> {
                optional.set(Optional.of(new Wrapper(storage)));
            });
        }

        return optional.get();
    }

    public static long moveBetweenBlockEntities(Level level, BlockPos from, Direction fromSide, BlockPos to, Direction toSide, long maxAmount) {
        BlockEntity fromBE = level.getBlockEntity(from);
        BlockEntity toBE = level.getBlockEntity(to);

        if (fromBE == null || toBE == null) {
            return 0;
        }

        AtomicLong result = new AtomicLong();

        fromBE.getCapability(CapabilityEnergy.ENERGY, fromSide).ifPresent(fromStorage -> toBE.getCapability(CapabilityEnergy.ENERGY, toSide).ifPresent(toStorage -> {
            int maxExtracted = fromStorage.extractEnergy((int) maxAmount, true);
            long accepted = toStorage.receiveEnergy(maxExtracted, false);
            result.set(fromStorage.extractEnergy((int) accepted, false));
        }));

        return result.get();
    }

    public record Wrapper(net.minecraftforge.energy.IEnergyStorage forgeStorage) implements IEnergyStorage {

        @Override
        public boolean canInsert() {
            return this.forgeStorage.canReceive();
        }

        @Override
        public int insertEnergy(int maxAmount, boolean simulate) {
            return this.forgeStorage.receiveEnergy(maxAmount, simulate);
        }

        @Override
        public boolean canWithdraw() {
            return this.forgeStorage.canExtract();
        }

        @Override
        public int withdrawEnergy(int maxAmount, boolean simulate) {
            return this.forgeStorage.extractEnergy(maxAmount, simulate);
        }

        @Override
        public int getEnergyAmount() {
            return this.forgeStorage.getEnergyStored();
        }

        @Override
        public int getEnergyCapacity() {
            return this.forgeStorage.getMaxEnergyStored();
        }
    }

}
