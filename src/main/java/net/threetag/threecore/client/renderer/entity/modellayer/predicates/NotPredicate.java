package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class NotPredicate implements IModelLayerPredicate {

    public final IModelLayerPredicate predicate;

    public NotPredicate(IModelLayerPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(IModelLayerContext context) {
        return !this.predicate.test(context);
    }
}
