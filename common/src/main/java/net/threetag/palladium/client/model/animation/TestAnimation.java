package net.threetag.palladium.client.model.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public class TestAnimation extends Animation {

    @Override
    public int getPriority() {
        return 20;
    }

    @Override
    public boolean active(LivingEntity entity) {
        return entity.isCrouching();
    }

    public float getProgress(LivingEntity entity, float partialTicks) {
        return Mth.sin((entity.tickCount + partialTicks) / 10F) / 2F + 0.5F;
    }

    @Override
    public void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-20F * getProgress(player, partialTicks)));
    }

    @Override
    public void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks) {
        float progress = getProgress(entity, partialTicks);
        model.head.xRot += (float) Math.toRadians(-20F * progress);
        model.body.xRot = 0.0F;
        model.rightLeg.z = 0.1F;
        model.leftLeg.z = 0.1F;
        model.rightLeg.y = 12.0F;
        model.leftLeg.y = 12.0F;
        model.head.y = 0.0F;
        model.body.y = 0.0F;
        model.leftArm.y = 2.0F;
        model.rightArm.y = 2.0F;

        model.rightArm.xRot = model.leftArm.xRot = 0F;
        model.hat.copyFrom(model.head);
        model.rightArm.zRot = (float) Math.toRadians(115F * progress);
        model.leftArm.zRot = (float) Math.toRadians(-115F * progress);

        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            model.leftLeg.xRot = 0F;
            model.rightLeg.yRot = (float) Math.toRadians(5F * progress);
            model.rightLeg.xRot = (float) Math.toRadians(-60F * progress);
            model.leftLeg.yRot = (float) Math.toRadians(-7.5F * progress);

            model.leftLeg.x += progress;
            model.leftLeg.y -= 3F * progress;
            model.leftLeg.z -= 3.1F * progress;
        } else {
            model.rightLeg.xRot = 0F;
            model.leftLeg.yRot = (float) Math.toRadians(-5F * progress);
            model.leftLeg.xRot = (float) Math.toRadians(-60F * progress);
            model.rightLeg.yRot = (float) Math.toRadians(7.5F * progress);

            model.rightLeg.x -= progress;
            model.rightLeg.y -= 3F * progress;
            model.rightLeg.z -= 3.1F * progress;
        }
    }
}
