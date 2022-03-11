package net.threetag.palladium.energy;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnergyHelper {

    @ExpectPlatform
    public static Optional<IEnergyStorage> getFromBlockEntity(Level level, BlockPos pos, @Nullable Direction side) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static long moveBetweenBlockEntities(Level level, BlockPos from, Direction fromSide, BlockPos to, Direction toSide, long maxAmount) {
        throw new AssertionError();
    }

}
