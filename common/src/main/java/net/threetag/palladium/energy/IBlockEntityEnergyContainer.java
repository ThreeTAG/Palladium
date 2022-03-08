package net.threetag.palladium.energy;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public interface IBlockEntityEnergyContainer {

    IEnergyStorage getEnergyStorage(@Nullable Direction side);

}
