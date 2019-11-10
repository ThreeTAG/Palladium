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
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        this.bipedLeftArm.showModel = entityIn.getShowArms();
        this.bipedRightArm.showModel = entityIn.getShowArms();
        this.bipedHead.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getX();
        this.bipedHead.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getY();
        this.bipedHead.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getZ();
        this.bipedBody.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getX();
        this.bipedBody.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getY();
        this.bipedBody.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getZ();
        this.bipedLeftArm.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getX();
        this.bipedLeftArm.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getY();
        this.bipedLeftArm.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getZ();
        this.bipedRightArm.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getX();
        this.bipedRightArm.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getY();
        this.bipedRightArm.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getZ();
        this.bipedLeftLeg.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getX();
        this.bipedLeftLeg.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getY();
        this.bipedLeftLeg.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getZ();
        this.bipedRightLeg.rotateAngleX = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getX();
        this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getY();
        this.bipedRightLeg.rotateAngleZ = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getZ();
        this.bipedHeadwear.copyModelAngles(this.bipedHead);
        this.basePlate.showModel = !entityIn.hasNoBasePlate();
        this.basePlate.rotateAngleX = 0.0F;
        this.basePlate.rotateAngleY = ((float) Math.PI / 180F) * -entityIn.rotationYaw;
        this.basePlate.rotateAngleZ = 0.0F;
    }
}