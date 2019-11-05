package net.threetag.threecore.util.modellayer;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.LazyLoadBase;
import net.threetag.threecore.util.client.RenderUtil;
import net.threetag.threecore.util.client.model.ISlotDependentVisibility;
import net.threetag.threecore.util.modellayer.predicates.IModelLayerPredicate;
import net.threetag.threecore.util.modellayer.texture.ModelLayerTexture;

import java.util.List;
import java.util.Objects;

public class ModelLayer {

    public final LazyLoadBase<BipedModel> model;
    public final ModelLayerTexture texture;
    public final boolean glow;
    public final List<IModelLayerPredicate> predicateList = Lists.newLinkedList();

    public ModelLayer(LazyLoadBase<BipedModel> model, ModelLayerTexture texture, boolean glow) {
        this.model = Objects.requireNonNull(model);
        this.texture = Objects.requireNonNull(texture);
        this.glow = glow;
    }

    public void render(IModelLayerContext context, IEntityRenderer entityRenderer, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Minecraft.getInstance().getTextureManager().bindTexture(this.getTexture(context).getTexture(context));
        BipedModel model = getModel(context);
        if (model != null) {
            entityRenderer.getEntityModel().setModelAttributes(model);
            model.isSneak = context.getAsEntity().shouldRenderSneaking();
            model.rightArmPose = ((BipedModel)entityRenderer.getEntityModel()).rightArmPose;
            model.leftArmPose = ((BipedModel)entityRenderer.getEntityModel()).leftArmPose;
            if (context.getSlot() != null)
                this.setModelSlotVisible(model, context.getSlot());
            if (this.glow) {
                RenderHelper.disableStandardItemLighting();
                RenderUtil.setLightmapTextureCoords(240, 240);
            }
            model.setLivingAnimations(context.getAsEntity(), limbSwing, limbSwingAmount, partialTicks);
            model.render(context.getAsEntity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            if (this.glow) {
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

}
