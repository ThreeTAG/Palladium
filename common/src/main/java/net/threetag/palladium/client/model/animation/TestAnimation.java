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
//        return 1F;
        return Mth.sin((entity.tickCount + partialTicks) / 10F) / 2F + 0.5F;
    }

    @Override
    public void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-20F * getProgress(player, partialTicks)));
    }

    @Override
    public void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks) {
        float progress = getProgress(entity, partialTicks);
        AnimationUtil.interpolateXRotTo(model.head, model.head.xRot +  (float) Math.toRadians(-20F * progress), progress);
        AnimationUtil.interpolateXRotTo(model.body, 0, progress);
        AnimationUtil.interpolateZTo(model.rightLeg, 0.1F, progress);
        AnimationUtil.interpolateZTo(model.leftLeg, 0.1F, progress);
        AnimationUtil.interpolateYTo(model.rightLeg, 12F, progress);
        AnimationUtil.interpolateYTo(model.leftLeg, 12F, progress);
        AnimationUtil.interpolateYTo(model.head, 0F, progress);
        AnimationUtil.interpolateYTo(model.body, 0F, progress);
        AnimationUtil.interpolateYTo(model.leftArm, 2F, progress);
        AnimationUtil.interpolateYTo(model.rightArm, 2F, progress);

        AnimationUtil.interpolateXRotTo(model.rightArm, 0, progress);
        AnimationUtil.interpolateXRotTo(model.leftArm, 0, progress);
        model.hat.copyFrom(model.head);
        AnimationUtil.interpolateZRotTo(model.rightArm, (float) Math.toRadians(115F), progress);
        AnimationUtil.interpolateZRotTo(model.leftArm, (float) Math.toRadians(-115F), progress);

        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            AnimationUtil.interpolateXRotTo(model.leftLeg, 0F, progress);
            AnimationUtil.interpolateYRotTo(model.rightLeg, (float) Math.toRadians(5F), progress);
            AnimationUtil.interpolateXRotTo(model.rightLeg, (float) Math.toRadians(-60F), progress);
            AnimationUtil.interpolateYRotTo(model.leftLeg, (float) Math.toRadians(-7.5F), progress);

            AnimationUtil.interpolateXTo(model.leftLeg, model.leftLeg.x + 1F, progress);
            AnimationUtil.interpolateYTo(model.leftLeg, model.leftLeg.y - 3F, progress);
            AnimationUtil.interpolateZTo(model.leftLeg, model.leftLeg.z - 3.1F, progress);
        } else {
            AnimationUtil.interpolateXRotTo(model.rightLeg, 0F, progress);
            AnimationUtil.interpolateYRotTo(model.leftLeg, (float) Math.toRadians(-5F), progress);
            AnimationUtil.interpolateXRotTo(model.leftLeg, (float) Math.toRadians(-60F), progress);
            AnimationUtil.interpolateYRotTo(model.rightLeg, (float) Math.toRadians(7.5F), progress);

            AnimationUtil.interpolateXTo(model.rightLeg, model.leftLeg.x - 1F, progress);
            AnimationUtil.interpolateYTo(model.rightLeg, model.leftLeg.y - 3F, progress);
            AnimationUtil.interpolateZTo(model.rightLeg, model.leftLeg.z - 3.1F, progress);
        }
    }
}
