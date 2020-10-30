package net.threetag.threecore.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.ArmorStandArmorModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.client.renderer.entity.layer.ColorableSuitStandLayer;
import net.threetag.threecore.client.renderer.entity.model.SuitStandBasePlateModel;
import net.threetag.threecore.client.renderer.entity.model.SuitStandModel;
import net.threetag.threecore.entity.SuitStandEntity;

import javax.annotation.Nullable;

public class SuitStandRenderer extends LivingRenderer<SuitStandEntity, SuitStandBasePlateModel> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/entity/suit_stand.png");

    public SuitStandRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new SuitStandBasePlateModel(0F), 0.0F);
        this.addLayer(new ColorableSuitStandLayer(this, new SuitStandModel()));
        this.addLayer(new BipedArmorLayer(this, new ArmorStandArmorModel(0.5F), new ArmorStandArmorModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new HeadLayer<>(this));
    }

    @Override
    protected void preRenderCallback(SuitStandEntity entity, MatrixStack matrixStack, float partialTickTime) {
        float scale = 0.9375F;
        matrixStack.scale(scale, scale, scale);
        if (!entity.hasNoBasePlate())
            matrixStack.translate(0, -1F / 16F, 0F);
    }

    @Override
    protected void applyRotations(SuitStandEntity entity, MatrixStack matrixStack, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        float lvt_6_1_ = (float) (entity.world.getGameTime() - entity.punchCooldown) + partialTicks;
        if (lvt_6_1_ < 5.0F) {
            matrixStack.rotate(Vector3f.YP.rotationDegrees(MathHelper.sin(lvt_6_1_ / 1.5F * 3.1415927F) * 3.0F));
        }
    }

    @Override
    protected boolean canRenderName(SuitStandEntity entity) {
        double distance = this.renderManager.squareDistanceTo(entity);
        float lvt_4_1_ = entity.isCrouching() ? 32.0F : 64.0F;
        return !(distance >= (double) (lvt_4_1_ * lvt_4_1_)) && entity.isCustomNameVisible();
    }

    @Nullable
    protected RenderType func_230496_a_(SuitStandEntity entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
        if (!entity.hasMarker()) {
            return super.func_230496_a_(entity, p_230496_2_, p_230496_3_, p_230496_4_);
        } else {
            ResourceLocation resourcelocation = this.getEntityTexture(entity);
            if (p_230496_3_) {
                return RenderType.getEntityTranslucent(resourcelocation, false);
            } else {
                return p_230496_2_ ? RenderType.getEntityCutoutNoCull(resourcelocation, false) : null;
            }
        }
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(SuitStandEntity entity) {
        return TEXTURE;
    }
}
