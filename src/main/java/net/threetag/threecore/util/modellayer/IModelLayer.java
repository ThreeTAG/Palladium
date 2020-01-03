package net.threetag.threecore.util.modellayer;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.threetag.threecore.util.modellayer.predicates.IModelLayerPredicate;

import javax.annotation.Nullable;

public interface IModelLayer {

    void render(IModelLayerContext context, @Nullable IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);

    boolean isActive(IModelLayerContext context);

    IModelLayer addPredicate(IModelLayerPredicate predicate);

}
