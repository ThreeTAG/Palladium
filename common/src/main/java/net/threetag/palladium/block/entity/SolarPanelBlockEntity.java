package net.threetag.palladium.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.energy.EnergyHelper;
import net.threetag.palladium.energy.EnergyStorage;
import net.threetag.palladium.energy.IBlockEntityEnergyContainer;
import net.threetag.palladium.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

public class SolarPanelBlockEntity extends BlockEntity implements IBlockEntityEnergyContainer {

    public EnergyStorage energyStorage = new EnergyStorage(20000, 0, 20);

    public SolarPanelBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(PalladiumBlockEntityTypes.SOLAR_PANEL.get(), blockPos, blockState);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, SolarPanelBlockEntity pBlockEntity) {
        if (pBlockEntity.canProduce()) {
            pBlockEntity.energyStorage.modifyEnergy(1);
        }

        if (pBlockEntity.energyStorage.getEnergyAmount() > 0) {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.UP) {
                    EnergyHelper.moveBetweenBlockEntities(pLevel, pPos, null, pPos.relative(direction), direction.getOpposite(), pBlockEntity.energyStorage.getEnergyAmount());
                }
            }
        }
    }

    public boolean canProduce() {
        return level.dimensionType().hasSkyLight() && level.canSeeSkyFromBelowWater(this.getBlockPos().above()) && !level.isRaining() && !level.isThundering() && level.isDay();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.energyStorage.deserializeNBT(tag.get("Energy"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("Energy", this.energyStorage.serializeNBT());
    }

    @Override
    public IEnergyStorage getEnergyStorage(@Nullable Direction side) {
        return side == null || side == Direction.DOWN ? this.energyStorage : null;
    }
}
