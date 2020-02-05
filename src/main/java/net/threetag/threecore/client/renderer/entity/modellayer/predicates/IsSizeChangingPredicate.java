package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

import java.util.concurrent.atomic.AtomicBoolean;

public class IsSizeChangingPredicate implements IModelLayerPredicate {

    @Override
    public boolean test(IModelLayerContext context) {
        AtomicBoolean b = new AtomicBoolean(false);
        context.getAsEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            b.set(sizeChanging.isSizeChanging());
        });
        return b.get();
    }
}
