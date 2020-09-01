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
       return this.tag.getAllElements().contains(context.getAsEntity().getType());
    }

}
