package net.threetag.threecore.client.renderer.entity.modellayer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.client.renderer.entity.model.CapeModel;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;
import net.threetag.threecore.client.renderer.entity.modellayer.texture.ModelLayerTexture;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CapeModelLayer implements IModelLayer {

    public final ModelLayerTexture texture;
    public final List<IModelLayerPredicate> glowPredicates;
    public final List<IModelLayerPredicate> predicateList = Lists.newLinkedList();

    public CapeModelLayer(ModelLayerTexture texture) {
        this.texture = texture;
        this.glowPredicates = Collections.emptyList();
    }

    public CapeModelLayer(ModelLayerTexture texture, List<IModelLayerPredicate> glowPredicates) {
        this.texture = texture;
        this.glowPredicates = glowPredicates;
    }

    @Override
    public void render(IModelLayerContext context, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, @Nullable IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityRenderer != null && entityRenderer.getEntityModel() instanceof BipedModel) {
            matrixStack.push();

            if (context.getAsEntity() instanceof PlayerEntity) {
                PlayerEntity entity = (PlayerEntity) context.getAsEntity();
                double d0 = MathHelper.lerp(partialTicks, entity.prevChasingPosX, entity.chasingPosX) - MathHelper.lerp(partialTicks, entity.prevPosX, entity.getPosX());
                double d1 = MathHelper.lerp(partialTicks, entity.prevChasingPosY, entity.chasingPosY) - MathHelper.lerp(partialTicks, entity.prevPosY, entity.getPosY());
                double d2 = MathHelper.lerp(partialTicks, entity.prevChasingPosZ, entity.chasingPosZ) - MathHelper.lerp(partialTicks, entity.prevPosZ, entity.getPosZ());
                float f = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset);
                double d3 = MathHelper.sin(f * ((float) Math.PI / 180F));
                double d4 = -MathHelper.cos(f * ((float) Math.PI / 180F));
                float f1 = (float) d1 * 10.0F;
                f1 = MathHelper.clamp(f1, -6.0F, 32.0F);
                float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
                f2 = MathHelper.clamp(f2, 0.0F, 150.0F);
                if (f2 < 0.0F) {
                    f2 = 0.0F;
                }

                float f4 = MathHelper.lerp(partialTicks, entity.prevCameraYaw, entity.cameraYaw);
                f1 = f1 + MathHelper.sin(MathHelper.lerp(partialTicks, entity.prevDistanceWalkedModified, entity.distanceWalkedModified) * 6.0F) * 32.0F * f4;

                CapeModel.INSTANCE.setCapeRotation(-(6.0F + f2 / 2.0F + f1));
            } else {
                CapeModel.INSTANCE.setCapeRotation(0);
            }

            ((BipedModel) entityRenderer.getEntityModel()).bipedBody.translateRotate(matrixStack);
            matrixStack.translate(0, -0.02F, 0.05F);
            matrixStack.scale(0.9F, 0.9F, 0.9F);
            CapeModel.INSTANCE.render(matrixStack, renderTypeBuffer.getBuffer(RenderType.getEntityTranslucentCull(this.texture.getTexture(context))), packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            matrixStack.pop();
        }
    }

    @Override
    public boolean isActive(IModelLayerContext context) {
        return ModelLayerManager.arePredicatesFulFilled(this.predicateList, context);
    }

    @Override
    public CapeModelLayer addPredicate(IModelLayerPredicate predicate) {
        this.predicateList.add(predicate);
        return this;
    }

    public static CapeModelLayer parse(JsonObject json) {
        List<IModelLayerPredicate> glowPredicates = Lists.newLinkedList();

        if (JSONUtils.hasField(json, "glow")) {
            JsonElement glowJson = json.get("glow");

            if (glowJson.isJsonPrimitive() && glowJson.getAsBoolean()) {
                glowPredicates.add((c) -> true);
            } else {
                JsonArray predicateArray = JSONUtils.getJsonArray(json, "glow");
                for (int i = 0; i < predicateArray.size(); i++) {
                    IModelLayerPredicate predicate = ModelLayerManager.parsePredicate(predicateArray.get(i).getAsJsonObject());
                    if (predicate != null)
                        glowPredicates.add(predicate);
                }
            }
        } else {
            glowPredicates.add((c) -> false);
        }

        return new CapeModelLayer(ModelLayerTexture.parse(json.get("texture")), glowPredicates);
    }
}
