package net.threetag.palladium.client.model;

import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Function;

public class ImprovedHumanoidModel<T extends LivingEntity> extends HumanoidModel<T> {

    public ImprovedHumanoidModel(ModelPart root) {
        super(root);
    }

    public ImprovedHumanoidModel(ModelPart root, Function<ResourceLocation, RenderType> renderType) {
        super(root, renderType);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.bodyParts().forEach(ModelPart::resetPose);
        this.headParts().forEach(ModelPart::resetPose);

        boolean bl = entity.getFallFlyingTicks() > 4;
        boolean bl2 = entity.isVisuallySwimming();
        this.head.yRot = netHeadYaw * 0.017453292F;
        if (bl) {
            this.head.xRot = -0.7853982F;
        } else if (this.swimAmount > 0.0F) {
            if (bl2) {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, -0.7853982F);
            } else {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, headPitch * 0.017453292F);
            }
        } else {
            this.head.xRot = headPitch * 0.017453292F;
        }

        this.body.yRot = 0.0F;
        float f = 1.0F;
        if (bl) {
            f = (float) entity.getDeltaMovement().lengthSqr();
            f /= 0.2F;
            f *= f * f;
        }

        if (f < 1.0F) {
            f = 1.0F;
        }

        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount * 0.5F / f;
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount / f;
        this.rightLeg.yRot = 0.005F;
        this.leftLeg.yRot = -0.005F;
        this.rightLeg.zRot = 0.005F;
        this.leftLeg.zRot = -0.005F;
        ModelPart var10000;
        if (this.riding) {
            var10000 = this.rightArm;
            var10000.xRot += -0.62831855F;
            var10000 = this.leftArm;
            var10000.xRot += -0.62831855F;
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = 0.31415927F;
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = -0.31415927F;
            this.leftLeg.zRot = -0.07853982F;
        }

        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        boolean bl3 = entity.getMainArm() == HumanoidArm.RIGHT;
        boolean bl4;
        if (entity.isUsingItem()) {
            bl4 = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (bl4 == bl3) {
                this.poseRightArm(entity);
            } else {
                this.poseLeftArm(entity);
            }
        } else {
            bl4 = bl3 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
            if (bl3 != bl4) {
                this.poseLeftArm(entity);
                this.poseRightArm(entity);
            } else {
                this.poseRightArm(entity);
                this.poseLeftArm(entity);
            }
        }

        this.setupAttackAnimation(entity, ageInTicks);
        if (this.crouching) {
            this.body.xRot = 0.5F;
            var10000 = this.rightArm;
            var10000.xRot += 0.4F;
            var10000 = this.leftArm;
            var10000.xRot += 0.4F;
            this.rightLeg.z += 4.0F;
            this.leftLeg.z += 4.0F;
            this.rightLeg.y += 0.2F;
            this.leftLeg.y += 0.2F;
            this.head.y += 4.2F;
            this.body.y += 3.2F;
            this.leftArm.y += 3.2F;
            this.rightArm.y += 3.2F;
        }

        if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.rightArm, ageInTicks, 1.0F);
        }

        if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.leftArm, ageInTicks, -1.0F);
        }

        if (this.swimAmount > 0.0F) {
            float g = limbSwing % 26.0F;
            HumanoidArm humanoidArm = this.getAttackArm(entity);
            float h = humanoidArm == HumanoidArm.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            float i = humanoidArm == HumanoidArm.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            float j;
            if (!entity.isUsingItem()) {
                if (g < 14.0F) {
                    this.leftArm.xRot = this.rotlerpRad(i, this.leftArm.xRot, 0.0F);
                    this.rightArm.xRot = Mth.lerp(h, this.rightArm.xRot, 0.0F);
                    this.leftArm.yRot = this.rotlerpRad(i, this.leftArm.yRot, 3.1415927F);
                    this.rightArm.yRot = Mth.lerp(h, this.rightArm.yRot, 3.1415927F);
                    this.leftArm.zRot = this.rotlerpRad(i, this.leftArm.zRot, 3.1415927F + 1.8707964F * this.quadraticArmUpdate(g) / this.quadraticArmUpdate(14.0F));
                    this.rightArm.zRot = Mth.lerp(h, this.rightArm.zRot, 3.1415927F - 1.8707964F * this.quadraticArmUpdate(g) / this.quadraticArmUpdate(14.0F));
                } else if (g >= 14.0F && g < 22.0F) {
                    j = (g - 14.0F) / 8.0F;
                    this.leftArm.xRot = this.rotlerpRad(i, this.leftArm.xRot, 1.5707964F * j);
                    this.rightArm.xRot = Mth.lerp(h, this.rightArm.xRot, 1.5707964F * j);
                    this.leftArm.yRot = this.rotlerpRad(i, this.leftArm.yRot, 3.1415927F);
                    this.rightArm.yRot = Mth.lerp(h, this.rightArm.yRot, 3.1415927F);
                    this.leftArm.zRot = this.rotlerpRad(i, this.leftArm.zRot, 5.012389F - 1.8707964F * j);
                    this.rightArm.zRot = Mth.lerp(h, this.rightArm.zRot, 1.2707963F + 1.8707964F * j);
                } else if (g >= 22.0F && g < 26.0F) {
                    j = (g - 22.0F) / 4.0F;
                    this.leftArm.xRot = this.rotlerpRad(i, this.leftArm.xRot, 1.5707964F - 1.5707964F * j);
                    this.rightArm.xRot = Mth.lerp(h, this.rightArm.xRot, 1.5707964F - 1.5707964F * j);
                    this.leftArm.yRot = this.rotlerpRad(i, this.leftArm.yRot, 3.1415927F);
                    this.rightArm.yRot = Mth.lerp(h, this.rightArm.yRot, 3.1415927F);
                    this.leftArm.zRot = this.rotlerpRad(i, this.leftArm.zRot, 3.1415927F);
                    this.rightArm.zRot = Mth.lerp(h, this.rightArm.zRot, 3.1415927F);
                }
            }

            j = 0.3F;
            float k = 0.33333334F;
            this.leftLeg.xRot = Mth.lerp(this.swimAmount, this.leftLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F + 3.1415927F));
            this.rightLeg.xRot = Mth.lerp(this.swimAmount, this.rightLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F));
        }

        this.hat.copyFrom(this.head);
    }
}
