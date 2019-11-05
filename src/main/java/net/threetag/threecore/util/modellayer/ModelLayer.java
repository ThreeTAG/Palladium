package net.threetag.threecore.util.modellayer;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.LazyLoadBase;
import net.threetag.threecore.util.client.RenderUtil;
import net.threetag.threecore.util.client.model.ISlotDependentVisibility;
import net.threetag.threecore.util.client.model.ModelRegistry;
import net.threetag.threecore.util.modellayer.predicates.IModelLayerPredicate;
import net.threetag.threecore.util.modellayer.texture.ModelLayerTexture;

import java.util.List;
import java.util.Objects;

public class ModelLayer {

    public final LazyLoadBase<BipedModel> model;
    public final ModelLayerTexture texture;
    public final List<IModelLayerPredicate> glowPredicates;
    public final List<IModelLayerPredicate> predicateList = Lists.newLinkedList();

    public ModelLayer(LazyLoadBase<BipedModel> model, ModelLayerTexture texture, List<IModelLayerPredicate> glowPredicates) {
        this.model = Objects.requireNonNull(model);
        this.texture = Objects.requireNonNull(texture);
        this.glowPredicates = glowPredicates;
    }

    public void render(IModelLayerContext context, IEntityRenderer entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Minecraft.getInstance().getTextureManager().bindTexture(this.getTexture(context).getTexture(context));
        BipedModel model = getModel(context);
        if (model != null) {
            boolean glow = ModelLayerManager.arePredicatesFulFilled(this.glowPredicates, context);
            entityRenderer.getEntityModel().setModelAttributes(model);
            model.isSneak = context.getAsEntity().shouldRenderSneaking();
            model.rightArmPose = ((BipedModel) entityRenderer.getEntityModel()).rightArmPose;
            model.leftArmPose = ((BipedModel) entityRenderer.getEntityModel()).leftArmPose;
            if (context.getSlot() != null)
                this.setModelSlotVisible(model, context.getSlot());
            if (glow) {
                RenderHelper.disableStandardItemLighting();
                RenderUtil.setLightmapTextureCoords(240, 240);
            }
            model.setLivingAnimations(context.getAsEntity(), limbSwing, limbSwingAmount, partialTicks);
            model.render(context.getAsEntity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (glow) {
                RenderUtil.restoreLightmapTextureCoords();
                RenderHelper.enableStandardItemLighting();
            }
        }
    }

    public BipedModel getModel(IModelLayerContext context) {
        return this.model.getValue();
    }

    public ModelLayerTexture getTexture(IModelLayerContext context) {
        return this.texture;
    }

    public boolean isActive(IModelLayerContext context) {
        return ModelLayerManager.arePredicatesFulFilled(this.predicateList, context);
    }

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

        return new ModelLayer(new LazyLoadBase<BipedModel>(() -> {
            Model model = ModelRegistry.getModel(JSONUtils.getString(json, "model"));
            return model instanceof BipedModel ? (BipedModel) model : null;
        }), ModelLayerTexture.parse(json.get("texture")), glowPredicates);
    }

}
