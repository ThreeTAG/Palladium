package net.threetag.threecore.util.armorlayer;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ModelArmorLayer extends ArmorLayer {

    public final LazyLoadBase<BipedModel> model;
    public final ResourceLocation texture;
    public final List<ArmorLayerManager.IArmorLayerPredicate> predicateList = Lists.newLinkedList();

    public ModelArmorLayer(LazyLoadBase<BipedModel> model, ResourceLocation texture) {
        this.model = model;
        this.texture = texture;
    }

    @Override
    public void render(ItemStack stack, LivingEntity entity, IEntityRenderer entityRenderer, EquipmentSlotType slot, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Minecraft.getInstance().getTextureManager().bindTexture(this.getTexture(stack, entity));
        BipedModel model = getModel(stack, entity);
        if (model != null) {
            entityRenderer.getEntityModel().setModelAttributes(model);
            model.isSneak = entity.isSneaking();
            this.setModelSlotVisible(model, slot);
            model.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
            model.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    public BipedModel getModel(ItemStack stack, LivingEntity entity) {
        return this.model.getValue();
    }

    public ResourceLocation getTexture(ItemStack stack, LivingEntity entity) {
        return this.texture;
    }

    @Override
    public boolean isActive(ItemStack stack, LivingEntity entity) {
        return ArmorLayerManager.arePredicatesFulFilled(this.predicateList, stack, entity);
    }

    @Override
    public ArmorLayer addPredicate(ArmorLayerManager.IArmorLayerPredicate predicate) {
        this.predicateList.add(predicate);
        return this;
    }

    protected void setModelSlotVisible(BipedModel model, EquipmentSlotType slotIn) {
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
