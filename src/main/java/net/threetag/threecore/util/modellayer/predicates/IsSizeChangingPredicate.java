package net.threetag.threecore.util.modellayer.predicates;

import net.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import net.threetag.threecore.util.modellayer.IModelLayerContext;
import net.threetag.threecore.util.modellayer.ModelLayerManager;

import java.util.concurrent.atomic.AtomicBoolean;

public class IsSizeChangingPredicate implements ModelLayerManager.IModelLayerPredicate {

    @Override
    public boolean test(IModelLayerContext context) {
        AtomicBoolean b = new AtomicBoolean(false);
        context.getAsEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            b.set(sizeChanging.isSizeChanging());
        });
        return b.get();
    }
}
