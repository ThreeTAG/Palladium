package net.threetag.threecore.util.modellayer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.LazyLoadBase;

public class GlowModelLayer extends ModelModelLayer {

    public GlowModelLayer(LazyLoadBase<BipedModel> model, ModelLayerTexture texture) {
        super(model, texture);
    }

    @Override
    public void render(ItemStack stack, LivingEntity entity, IEntityRenderer entityRenderer, EquipmentSlotType slot, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.disableLighting();
        // TODO lightmap coords
        super.render(stack, entity, entityRenderer, slot, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.enableLighting();
    }
}
