package net.threetag.threecore.util.modellayer.predicates;

import net.threetag.threecore.util.modellayer.IModelLayerContext;
import net.threetag.threecore.util.modellayer.ModelLayerManager;

public class NotPredicate implements ModelLayerManager.IModelLayerPredicate {

    public final ModelLayerManager.IModelLayerPredicate predicate;

    public NotPredicate(ModelLayerManager.IModelLayerPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(IModelLayerContext context) {
        return !this.predicate.test(context);
    }
}
