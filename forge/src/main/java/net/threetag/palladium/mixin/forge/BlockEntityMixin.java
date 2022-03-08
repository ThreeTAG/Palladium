package net.threetag.palladium.mixin.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.threetag.palladium.energy.IBlockEntityEnergyContainer;
import net.threetag.palladium.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin extends BlockEntity {

    public BlockEntityMixin(BlockEntityType<?> arg, BlockPos arg2, BlockState arg3) {
        super(arg, arg2, arg3);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY && this instanceof IBlockEntityEnergyContainer energy) {
            IEnergyStorage energyStorage = energy.getEnergyStorage(side);

            if (energyStorage != null) {
                return LazyOptional.of(() -> (net.minecraftforge.energy.IEnergyStorage) energyStorage).cast();
            }
        }
        return super.getCapability(cap, side);
    }
}
