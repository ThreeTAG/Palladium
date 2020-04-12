package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class EntityTypePredicate implements IModelLayerPredicate {

    public final ResourceLocation entityId;

    public EntityTypePredicate(ResourceLocation entityId) {
        this.entityId = entityId;
    }

    @Override
    public boolean test(IModelLayerContext context) {
        return context.getAsEntity().getType().getRegistryName().equals(this.entityId);
    }

}
