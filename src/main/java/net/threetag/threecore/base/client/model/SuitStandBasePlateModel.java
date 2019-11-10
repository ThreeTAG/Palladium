package net.threetag.threecore.base.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.threetag.threecore.base.entity.SuitStandEntity;

public class SuitStandBasePlateModel extends BipedModel<SuitStandEntity> {

    private final RendererModel basePlate;

    public SuitStandBasePlateModel() {
        this.textureHeight = this.textureWidth = 64;
        this.basePlate = new RendererModel(this, 0, 32);
        this.basePlate.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.basePlate.addBox(-6F, 0F, -6F, 12, 1, 12, 0F);
    }

    @Override
    public void render(SuitStandEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.basePlate.render(f5);
    }

    @Override
    public void setRotationAngles(SuitStandEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
        this.basePlate.showModel = !entityIn.hasNoBasePlate();
        this.basePlate.rotateAngleX = 0.0F;
        this.basePlate.rotateAngleY = ((float) Math.PI / 180F) * -entityIn.rotationYaw;
        this.basePlate.rotateAngleZ = 0.0F;
    }
}