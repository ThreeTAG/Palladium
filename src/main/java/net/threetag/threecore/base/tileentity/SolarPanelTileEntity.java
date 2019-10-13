package net.threetag.threecore.base.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.util.energy.EnergyStorageExt;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SolarPanelTileEntity extends TileEntity implements ITickableTileEntity {

    public EnergyStorageExt energyStorage = new EnergyStorageExt(ThreeCoreServerConfig.ENERGY.SOLAR_PANEL);
    private LazyOptional<IEnergyStorage> energyStorageLazyOptional = LazyOptional.of(() -> energyStorage);

    public SolarPanelTileEntity() {
        super(ThreeCoreBase.SOLAR_PANEL_TILE_ENTITY);
    }

    @Override
    public void tick() {
        if (this.world == null || this.world.isRemote)
            return;

        if (canProduce()) {
            this.energyStorage.receiveEnergy(1, false);
        }

        if (this.energyStorage.getEnergyStored() > 0) {
            for (Direction direction : Direction.values()) {
                if (direction != Direction.UP) {
                    TileEntity tileEntity = this.world.getTileEntity(this.getPos().offset(direction));

                    if (tileEntity != null) {
                        tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                            if (energyStorage.canReceive())
                                this.energyStorage.modifyEnergy(-energyStorage.receiveEnergy(Math.min(this.energyStorage.getEnergyStored(), this.energyStorage.getMaxExtract()), false));
                        });
                    }
                }
            }
        }
    }

    public boolean canProduce() {
        return world.dimension.hasSkyLight() && world.canBlockSeeSky(this.getPos().up()) && !world.isRaining() && !world.isThundering() && world.isDaytime();
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.energyStorage = new EnergyStorageExt(ThreeCoreServerConfig.ENERGY.SOLAR_PANEL, compound.getInt("Energy"));
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
