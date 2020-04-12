package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.Tag;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class EntityTagPredicate implements IModelLayerPredicate {

    public final Tag<EntityType<?>> tag;

    public EntityTagPredicate(Tag<EntityType<?>> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(IModelLayerContext context) {
        return this.tag.contains(context.getAsEntity().getType());
    }

}
