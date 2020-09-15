package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class EntityTagPredicate implements IModelLayerPredicate {

    public final ResourceLocation tagName;

    public EntityTagPredicate(ResourceLocation tagName) {
        this.tagName = tagName;
    }

    @Override
    public boolean test(IModelLayerContext context) {
        ITag<EntityType<?>> tag = EntityTypeTags.getCollection().get(this.tagName);
        return tag != null && tag.getAllElements().contains(context.getAsEntity().getType());
    }

}
