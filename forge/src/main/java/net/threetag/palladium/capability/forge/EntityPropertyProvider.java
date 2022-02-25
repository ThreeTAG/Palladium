package net.threetag.palladium.capability.forge;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityPropertyProvider implements ICapabilitySerializable<CompoundTag> {

    public final EntityPropertyHandler capability;
    public final LazyOptional<EntityPropertyHandler> lazyOptional;

    public EntityPropertyProvider(EntityPropertyHandler capability) {
        this.capability = capability;
        this.lazyOptional = LazyOptional.of(() -> capability);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == PalladiumCapabilities.ENTITY_PROPERTIES ? this.lazyOptional.cast() : LazyOptional.empty();
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
