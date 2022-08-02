package net.threetag.palladium.client.model.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.entity.PalladiumAttributes;

@Environment(EnvType.CLIENT)
public class FlightAnimation extends Animation {

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean active(LivingEntity entity) {
        return entity instanceof Player && !entity.isOnGround()
                && entity.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get()) <= 0D
                && entity.getAttributeValue(PalladiumAttributes.JETPACK_FLIGHT_SPEED.get()) > 0D;
    }

    @Override
    public void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        // TODO interpolation
        float speed = (float) Mth.clamp(Math
                .sqrt((player.xo - player.position().x) * (player.xo - player.position().x) + (player.zo - player.position().z) * (player.zo
                        - player.position().z)), 0F, 1F);
        poseStack.mulPose(Vector3f.XP.rotationDegrees(-60 * speed));
    }

    @Override
    public void setupAnimation(HumanoidModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float speed = (float) Mth.clamp(Math
                .sqrt((entity.xo - entity.position().x) * (entity.xo - entity.position().x) + (entity.zo - entity.position().z) * (entity.zo
                        - entity.position().z)), 0F, 1F);
        double d1 = 1 - speed;

        model.rightArm.xRot *= d1;
        model.rightArm.yRot *= d1;
        model.rightArm.zRot *= d1;
        model.leftArm.xRot *= d1;
        model.leftArm.yRot *= d1;
        model.leftArm.zRot *= d1;
        model.rightLeg.xRot *= d1;
        model.rightLeg.yRot *= d1;
        model.rightLeg.zRot *= d1;
        model.leftLeg.xRot *= d1;
        model.leftLeg.yRot *= d1;
        model.leftLeg.zRot *= d1;

        int type = (entity.isOnGround() || (entity instanceof Player && ((Player) entity).getAbilities().flying) ? 0 : entity.zza < 0 ? -1 : 1);
        speed *= type;
        model.head.xRot -= 0.52359877559829887307710723054658 * 2 * speed;
        model.hat.xRot = model.head.xRot;
    }
}
