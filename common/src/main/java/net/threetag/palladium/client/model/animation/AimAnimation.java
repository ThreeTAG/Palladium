package net.threetag.palladium.client.model.animation;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AimAbility;

public class AimAnimation extends Animation {

    @Override
    public int getPriority() {
        return 15;
    }

    @Override
    public boolean active(LivingEntity entity) {
        return AimAbility.getTimer(entity, 0, true) > 0F || AimAbility.getTimer(entity, 0, false) > 0F;
    }

    @Override
    public void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float partialTicks) {
        float right = AnimationUtil.smooth(AimAbility.getTimer(entity, partialTicks, true));
        float left = AnimationUtil.smooth(AimAbility.getTimer(entity, partialTicks, false));

        if(right > 0F) {
            AnimationUtil.interpolateXRotTo(model.rightArm, (float) (model.head.xRot - Math.PI / 2F), right);
            AnimationUtil.interpolateYRotTo(model.rightArm, model.head.yRot, right);
            AnimationUtil.interpolateZRotTo(model.rightArm, model.head.zRot, right);

            if(entity == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                AnimationUtil.interpolateXTo(model.rightArm, model.rightArm.x - 3.5F, right);
                AnimationUtil.interpolateZTo(model.rightArm, model.rightArm.z + 1.5F, right);
                AnimationUtil.interpolateYTo(model.rightArm, model.rightArm.y + 1.5F, right);
                AnimationUtil.interpolateXRotTo(model.rightArm, (float) (model.rightArm.xRot - Math.toRadians(20)), right);
                AnimationUtil.interpolateYRotTo(model.rightArm, (float) (model.rightArm.yRot - Math.toRadians(27)), right);
                AnimationUtil.interpolateZRotTo(model.rightArm, (float) (model.rightArm.zRot - Math.toRadians(30)), right);
            }
        }

        if(left > 0F) {
            AnimationUtil.interpolateXRotTo(model.leftArm, (float) (model.head.xRot - Math.PI / 2F), left);
            AnimationUtil.interpolateYRotTo(model.leftArm, model.head.yRot, left);
            AnimationUtil.interpolateZRotTo(model.leftArm, model.head.zRot, left);

            if(entity == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                AnimationUtil.interpolateXTo(model.leftArm, model.leftArm.x + 3.5F, left);
                AnimationUtil.interpolateZTo(model.leftArm, model.leftArm.z + 1.5F, left);
                AnimationUtil.interpolateYTo(model.leftArm, model.leftArm.y + 1.5F, left);
                AnimationUtil.interpolateXRotTo(model.leftArm, (float) (model.leftArm.xRot - Math.toRadians(20)), left);
                AnimationUtil.interpolateYRotTo(model.leftArm, (float) (model.leftArm.yRot + Math.toRadians(27)), left);
                AnimationUtil.interpolateZRotTo(model.leftArm, (float) (model.leftArm.zRot + Math.toRadians(30)), left);
            }
        }
    }
}
