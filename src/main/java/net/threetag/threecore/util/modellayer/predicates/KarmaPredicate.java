package net.threetag.threecore.util.modellayer.predicates;

import net.threetag.threecore.karma.capability.CapabilityKarma;
import net.threetag.threecore.util.modellayer.IModelLayerContext;

import java.util.concurrent.atomic.AtomicInteger;

public class KarmaPredicate implements IModelLayerPredicate {

    public final int min;
    public final int max;

    public KarmaPredicate(int min, int max) {
        this.min = min;
        this.max = max;

        if (min > max)
            throw new IllegalStateException("Min karma value cant be bigger than max karma value!");
    }

    @Override
    public boolean test(IModelLayerContext context) {
        AtomicInteger karmaVal = new AtomicInteger(0);
        context.getAsEntity().getCapability(CapabilityKarma.KARMA).ifPresent(karma -> karmaVal.set(karma.getKarma()));
        return karmaVal.get() >= min && karmaVal.get() <= max;
    }

}
