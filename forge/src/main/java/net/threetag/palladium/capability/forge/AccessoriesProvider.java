package net.threetag.palladium.capability.forge;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.threetag.palladium.accessory.AccessoryPlayerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AccessoriesProvider implements ICapabilitySerializable<CompoundTag> {

    public final AccessoryPlayerData capability;
    public final LazyOptional<AccessoryPlayerData> lazyOptional;

    public AccessoriesProvider(AccessoryPlayerData capability) {
        this.capability = capability;
        this.lazyOptional = LazyOptional.of(() -> this.capability);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == PalladiumCapabilities.ACCESSORIES ? this.lazyOptional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.capability.toNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.capability.fromNBT(nbt);
    }
}
