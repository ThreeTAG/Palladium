package net.threetag.threecore.util.modellayer;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.LazyLoadBase;

public class GlowModelLayer extends ModelModelLayer {

    public GlowModelLayer(LazyLoadBase<BipedModel> model, ModelLayerTexture texture) {
        super(model, texture);
    }

    @Override
    public void render(ItemStack stack, LivingEntity entity, IEntityRenderer entityRenderer, EquipmentSlotType slot, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        // TODO lightmap coords
        GlStateManager.disableFog();
        RenderHelper.disableStandardItemLighting();
        super.render(stack, entity, entityRenderer, slot, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableFog();
    }
}
