package net.threetag.threecore.client.renderer.entity.modellayer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.HandSide;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;

import javax.annotation.Nullable;

public interface IModelLayer {

    void render(IModelLayerContext context, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, @Nullable IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    default void renderArm(HandSide handSide, IModelLayerContext context, PlayerRenderer playerRenderer, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight) {

    }

    boolean isActive(IModelLayerContext context);

    IModelLayer addPredicate(IModelLayerPredicate predicate);

}
