package net.threetag.palladium.capability.forge;

import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.threetag.palladium.power.PowerHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PowerProvider implements ICapabilityProvider {

    public final LazyOptional<PowerHandler> lazyOptional;

    public PowerProvider(PowerHandler capability) {
        this.lazyOptional = LazyOptional.of(() -> capability);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
        return capability == PalladiumCapabilities.POWER_HANDLER ? this.lazyOptional.cast() : LazyOptional.empty();
    }

}
