package net.threetag.threecore.client.renderer.entity.modellayer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.threetag.threecore.util.RenderUtil;
import net.threetag.threecore.client.renderer.entity.model.ISlotDependentVisibility;
import net.threetag.threecore.client.renderer.entity.model.ModelRegistry;
import net.threetag.threecore.client.renderer.entity.modellayer.predicates.IModelLayerPredicate;
import net.threetag.threecore.client.renderer.entity.modellayer.texture.ModelLayerTexture;

import java.util.List;
import java.util.Objects;

public class ModelLayer implements IModelLayer {

    public final LazyLoadBase<Model> model;
    public final ModelLayerTexture texture;
    public final List<IModelLayerPredicate> glowPredicates;
    public final List<IModelLayerPredicate> predicateList = Lists.newLinkedList();

    public ModelLayer(LazyLoadBase<Model> model, ModelLayerTexture texture, List<IModelLayerPredicate> glowPredicates) {
        this.model = Objects.requireNonNull(model);
        this.texture = Objects.requireNonNull(texture);
        this.glowPredicates = glowPredicates;
    }

    @Override
    public void render(IModelLayerContext context, IEntityRenderer<? extends Entity, ? extends EntityModel<?>> entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Minecraft.getInstance().getTextureManager().bindTexture(this.getTexture(context).getTexture(context));
        Model model = getModel(context);
        boolean glow = ModelLayerManager.arePredicatesFulFilled(this.glowPredicates, context);

        if (glow) {
            RenderHelper.disableStandardItemLighting();
            RenderUtil.setLightmapTextureCoords(240, 240);
        }

        if (model instanceof BipedModel && context.getAsEntity() instanceof LivingEntity) {
            BipedModel bipedModel = (BipedModel) model;
            entityRenderer.getEntityModel().setModelAttributes(bipedModel);
            bipedModel.isSneak = context.getAsEntity().shouldRenderSneaking();
            if (entityRenderer != null) {
                bipedModel.rightArmPose = ((BipedModel) entityRenderer.getEntityModel()).rightArmPose;
                bipedModel.leftArmPose = ((BipedModel) entityRenderer.getEntityModel()).leftArmPose;
            }
            if (context.getSlot() != null)
                this.setModelSlotVisible(bipedModel, context.getSlot());
            bipedModel.setLivingAnimations((LivingEntity) context.getAsEntity(), limbSwing, limbSwingAmount, partialTicks);
            bipedModel.render((LivingEntity) context.getAsEntity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        } else if (model instanceof EntityModel) {
            ((EntityModel) model).setLivingAnimations(context.getAsEntity(), limbSwing, limbSwingAmount, partialTicks);
            ((EntityModel) model).render(context.getAsEntity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }

        if (glow) {
            RenderUtil.restoreLightmapTextureCoords();
            RenderHelper.enableStandardItemLighting();
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
                glowPredicates.add((c) -> false);
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

        return new ModelLayer(new LazyLoadBase<>(() -> ModelRegistry.getModel(JSONUtils.getString(json, "model"))), ModelLayerTexture.parse(json.get("texture")), glowPredicates);
    }

}
