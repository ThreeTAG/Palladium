package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

import java.util.concurrent.atomic.AtomicReference;

public class SizePredicate implements IModelLayerPredicate {

    public final float min;
    public final float max;

    public SizePredicate(float min, float max) {
        this.min = MathHelper.clamp(min, 0F, Integer.MAX_VALUE);
        this.max = MathHelper.clamp(max, 0F, Integer.MAX_VALUE);

        if (min > max)
            throw new IllegalStateException("Min size cant be bigger than max size!");
    }

    @Override
    public boolean test(IModelLayerContext context) {
        AtomicReference<Float> size = new AtomicReference<>(1F);
        context.getAsEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> size.set(sizeChanging.getScale()));
        return size.get() >= min && size.get() <= max;
    }

}
