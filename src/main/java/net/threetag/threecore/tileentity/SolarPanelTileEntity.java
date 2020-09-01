package net.threetag.threecore.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.util.energy.EnergyStorageExt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SolarPanelTileEntity extends TileEntity implements ITickableTileEntity {

    public EnergyStorageExt energyStorage = EnergyStorageExt.noReceive(ThreeCoreServerConfig.ENERGY.SOLAR_PANEL);
    private LazyOptional<IEnergyStorage> energyStorageLazyOptional = LazyOptional.of(() -> energyStorage);

    public SolarPanelTileEntity() {
        super(TCTileEntityTypes.SOLAR_PANEL.get());
    }

    @Override
    public void tick() {
        if (this.world == null || this.world.isRemote)
            return;

        if (canProduce()) {
            this.energyStorage.modifyEnergy(ThreeCoreServerConfig.ENERGY.SOLAR_PANEL_PRODUCTION.get());
        }

        if (this.energyStorage.getEnergyStored() > 0) {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.UP) {
                    TileEntity tileEntity = this.world.getTileEntity(this.getPos().offset(direction));

                    if (tileEntity != null) {
                        tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(energyStorage -> {
                            if (energyStorage.canReceive()) {
                                this.energyStorage.modifyEnergy(-energyStorage.receiveEnergy(Math.min(this.energyStorage.getEnergyStored(), this.energyStorage.getMaxExtract()), false));
                            }
                        });
                    }
                }
            }
        }
    }

    public boolean canProduce() {
        return world.func_230315_m_().hasSkyLight() && world.canBlockSeeSky(this.getPos().up()) && !world.isRaining() && !world.isThundering() && world.isDaytime();
    }

    @Override
    public void read(BlockState blockState, CompoundNBT compound) {
        super.read(blockState, compound);
        this.energyStorage = EnergyStorageExt.noReceive(ThreeCoreServerConfig.ENERGY.SOLAR_PANEL, compound.getInt("Energy"));

    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Energy", this.energyStorage.getEnergyStored());
        return super.write(compound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityEnergy.ENERGY && (side == Direction.DOWN || side == null) ? this.energyStorageLazyOptional.cast() : super.getCapability(cap, side);
    }
}
