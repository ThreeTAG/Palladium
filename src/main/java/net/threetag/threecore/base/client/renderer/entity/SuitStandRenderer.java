package net.threetag.threecore.base.client.renderer.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.ArmorStandArmorModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.client.model.SuitStandBasePlateModel;
import net.threetag.threecore.base.client.model.SuitStandModel;
import net.threetag.threecore.base.client.renderer.entity.layer.ColorableSuitStandLayer;
import net.threetag.threecore.base.entity.SuitStandEntity;

import javax.annotation.Nullable;

public class SuitStandRenderer extends LivingRenderer<SuitStandEntity, SuitStandBasePlateModel> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/entity/suit_stand.png");

    public SuitStandRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new SuitStandBasePlateModel(), 0.0F);
        this.addLayer(new ColorableSuitStandLayer(this, new SuitStandModel()));
        this.addLayer(new BipedArmorLayer(this, new ArmorStandArmorModel(0.5F), new ArmorStandArmorModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new HeadLayer<>(this));
    }

    @Override
    protected void preRenderCallback(SuitStandEntity entitylivingbaseIn, float partialTickTime) {
        if (!entitylivingbaseIn.hasNoBasePlate())
            GlStateManager.translatef(0, -1F / 16F, 0F);
    }

    @Override
    protected void applyRotations(SuitStandEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        GlStateManager.rotatef(180.0F - rotationYaw, 0.0F, 1.0F, 0.0F);
        float f = (float) (entityLiving.world.getGameTime() - entityLiving.punchCooldown) + partialTicks;
        if (f < 5.0F) {
            GlStateManager.rotatef(MathHelper.sin(f / 1.5F * (float) Math.PI) * 3.0F, 0.0F, 1.0F, 0.0F);
        }
    }

    @Override
    protected boolean canRenderName(SuitStandEntity entity) {
        return entity.isCustomNameVisible();
    }

    @Override
    public void doRender(SuitStandEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.hasMarker()) {
            this.renderMarker = true;
        }

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        if (entity.hasMarker()) {
            this.renderMarker = false;
        }

    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(SuitStandEntity entity) {
        return TEXTURE;
    }
}
