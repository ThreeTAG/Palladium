package net.threetag.threecore.fluid;

import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.threetag.threecore.util.TCFluidUtil;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class FluidTankExt extends FluidTank {

    private Consumer<SoundEvent> soundHandler;
    private Consumer<FluidStack> changedCallback;

    public FluidTankExt(int capacity) {
        super(capacity);
    }

    public FluidTankExt(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    public FluidTankExt setSoundHandler(Consumer<SoundEvent> soundHandler) {
        this.soundHandler = soundHandler;
        return this;
    }

    public FluidTankExt setCallback(Consumer<FluidStack> callback) {
        this.changedCallback = callback;
        return this;
    }

    @Override
    protected void onContentsChanged() {
        if (this.changedCallback != null)
            this.changedCallback.accept(this.getFluid());
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack stack = super.drain(maxDrain, action);
        if (!stack.isEmpty() && action.execute()) {
            this.onContentsChanged();
            if (this.soundHandler != null) {
                this.soundHandler.accept(TCFluidUtil.getSound(stack, false));
            }
        }
        return stack;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        int fill = super.fill(resource, action);
        if (fill > 0 && action.execute()) {
            this.onContentsChanged();
            if (this.soundHandler != null) {
                this.soundHandler.accept(TCFluidUtil.getSound(resource, true));
            }
        }
        return fill;
    }
}
