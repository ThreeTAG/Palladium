package net.threetag.threecore.client.renderer.entity.modellayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;

import javax.annotation.Nullable;

public interface IModelLayer {

    // TODO fix parameters
    void render(IModelLayerContext context, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int i, @Nullable IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    boolean isActive(IModelLayerContext context);

    IModelLayer addPredicate(IModelLayerPredicate predicate);

}
