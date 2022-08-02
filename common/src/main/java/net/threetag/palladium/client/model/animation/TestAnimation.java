package net.threetag.palladium.client.model.animation;

import net.minecraft.client.model.HumanoidModel;
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

    @Override
    public void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        model.head.xRot = (float) Math.toRadians(-20);
        model.head.y = 30;
    }
}
