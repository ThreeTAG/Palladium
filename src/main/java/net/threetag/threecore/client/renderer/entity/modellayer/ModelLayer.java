package net.threetag.threecore.client.renderer.entity.modellayer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.HandSide;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyValue;
import net.threetag.threecore.client.renderer.entity.model.IArmRenderingModel;
import net.threetag.threecore.client.renderer.entity.model.ISlotDependentVisibility;
import net.threetag.threecore.client.renderer.entity.model.ModelRegistry;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;
import net.threetag.threecore.client.renderer.entity.modellayer.texture.ModelLayerTexture;
import net.threetag.threecore.util.RenderUtil;

import java.util.List;
import java.util.Objects;

public class ModelLayer implements IModelLayer {

    public final LazyValue<Model> model;
    public final ModelLayerTexture texture;
    public final List<IModelLayerPredicate> glowPredicates;
    public final List<IModelLayerPredicate> predicateList = Lists.newLinkedList();

    public ModelLayer(LazyValue<Model> model, ModelLayerTexture texture, List<IModelLayerPredicate> glowPredicates) {
        this.model = Objects.requireNonNull(model);
        this.texture = Objects.requireNonNull(texture);
        this.glowPredicates = glowPredicates;
    }

    @Override
    public void render(IModelLayerContext context, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight, IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        Model model = getModel(context);

        if (model instanceof BipedModel && entityRenderer.getEntityModel() instanceof BipedModel && context.getAsEntity() instanceof LivingEntity) {

            BipedModel bipedModel = (BipedModel) model;
            ((BipedModel) entityRenderer.getEntityModel()).setModelAttributes(bipedModel);

            if (entityRenderer != null) {
                bipedModel.rightArmPose = ((BipedModel) entityRenderer.getEntityModel()).rightArmPose;
                bipedModel.leftArmPose = ((BipedModel) entityRenderer.getEntityModel()).leftArmPose;
            }

            if (context.getSlot() != null)
                this.setModelSlotVisible(bipedModel, context.getSlot());

            bipedModel.setLivingAnimations((LivingEntity) context.getAsEntity(), limbSwing, limbSwingAmount, partialTicks);
            bipedModel.setRotationAngles((LivingEntity) context.getAsEntity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            boolean glow = ModelLayerManager.arePredicatesFulFilled(this.glowPredicates, context);
            IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(renderTypeBuffer, glow ? RenderUtil.RenderTypes.getGlowing(this.getTexture(context).getTexture(context)) : RenderType.getEntityTranslucent(this.getTexture(context).getTexture(context)), false, false);
            bipedModel.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

        } else if (model instanceof EntityModel) {

            // TODO
//            ((EntityModel) model).setLivingAnimations(context.getAsEntity(), limbSwing, limbSwingAmount, partialTicks);
//            IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(renderTypeBuffer,  glow ? RenderUtil.RenderTypes.getGlowing(this.getTexture(context).getTexture(context)) : RenderUtil.RenderTypes.getEntityTranslucent(this.getTexture(context).getTexture(context)), false, false);
//            model.render(matrixStack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);

        }
    }

    @Override
    public void renderArm(HandSide handSide, IModelLayerContext context, PlayerRenderer playerRenderer, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        Model model = getModel(context);

        if (model instanceof BipedModel && context.getAsEntity() instanceof PlayerEntity) {
            BipedModel bipedModel = (BipedModel) model;
            bipedModel.swingProgress = 0.0F;
            bipedModel.isSneak = false;
            bipedModel.swimAnimation = 0.0F;
            bipedModel.setRotationAngles((LivingEntity) context.getAsEntity(), 0F, 0F, 0F, 0F, 0F);
            boolean glow = ModelLayerManager.arePredicatesFulFilled(this.glowPredicates, context);
            IVertexBuilder vertexBuilder = ItemRenderer.getBuffer(buffer, glow ? RenderUtil.RenderTypes.getGlowing(this.getTexture(context).getTexture(context)) : RenderType.getEntityTranslucent(this.getTexture(context).getTexture(context)), false, false);

            if (bipedModel instanceof IArmRenderingModel) {
                ((IArmRenderingModel) bipedModel).renderArm(handSide, matrixStack, vertexBuilder, packedLight);
            } else {

                if (handSide == HandSide.RIGHT) {
                    bipedModel.bipedRightArm.rotateAngleX = 0.0F;
                    bipedModel.bipedRightArm.render(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY);
                } else {
                    bipedModel.bipedLeftArm.rotateAngleX = 0.0F;
                    bipedModel.bipedLeftArm.render(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY);
                }
            }
        }
    }

    public Model getModel(IModelLayerContext context) {
        return this.model.getValue();
    }

    public ModelLayerTexture getTexture(IModelLayerContext context) {
        return this.texture;
    }

    @Override
    public boolean isActive(IModelLayerContext context) {
        return ModelLayerManager.arePredicatesFulFilled(this.predicateList, context);
    }

    @Override
    public ModelLayer addPredicate(IModelLayerPredicate predicate) {
        this.predicateList.add(predicate);
        return this;
    }

    protected void setModelSlotVisible(BipedModel model, EquipmentSlotType slotIn) {
        if (model instanceof ISlotDependentVisibility)
            ((ISlotDependentVisibility) model).setSlotVisibility(slotIn);
        else {
            model.setVisible(false);
            switch (slotIn) {
                case HEAD:
                    model.bipedHead.showModel = true;
                    model.bipedHeadwear.showModel = true;
                    break;
                case CHEST:
                    model.bipedBody.showModel = true;
                    model.bipedRightArm.showModel = true;
                    model.bipedLeftArm.showModel = true;
                    break;
                case LEGS:
                    model.bipedBody.showModel = true;
                    model.bipedRightLeg.showModel = true;
                    model.bipedLeftLeg.showModel = true;
                    break;
                case FEET:
                    model.bipedRightLeg.showModel = true;
                    model.bipedLeftLeg.showModel = true;
            }
        }

    }

    public static ModelLayer parse(JsonObject json) {
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

        return new ModelLayer(new LazyValue<>(() -> ModelRegistry.getModel(JSONUtils.getString(json, "model"))), ModelLayerTexture.parse(json.get("texture")), glowPredicates);
    }

}
