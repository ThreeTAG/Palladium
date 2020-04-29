package net.threetag.threecore.client.renderer.entity.modellayer;

import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.HandSide;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;

import javax.annotation.Nullable;

public interface IModelLayer {

    void render(IModelLayerContext context, @Nullable IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale);

    default void renderArm(HandSide handSide, IModelLayerContext context, PlayerRenderer playerRenderer) {

    }

    boolean isActive(IModelLayerContext context);

    IModelLayer addPredicate(IModelLayerPredicate predicate);

}
