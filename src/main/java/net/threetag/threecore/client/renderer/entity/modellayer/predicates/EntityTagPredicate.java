package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.ITag;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class EntityTagPredicate implements IModelLayerPredicate {

    public final ITag.INamedTag<EntityType<?>> tag;

    public EntityTagPredicate(ITag.INamedTag<EntityType<?>> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(IModelLayerContext context) {
       return this.tag.func_230236_b_().contains(context.getAsEntity().getType());
    }

}
