package net.threetag.palladium.client.model.animation;

import dev.kosmx.playerAnim.core.util.Ease;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;

@Environment(EnvType.CLIENT)
public class BasicFlightAnimation extends PalladiumAnimation {

    public BasicFlightAnimation(int priority) {
        super(priority);
    }

    @Override
    public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
//        boolean active = !player.isOnGround() && !firstPersonContext.firstPerson();
//
//        if (active && player instanceof PalladiumPlayerExtension extension) {
//            var flightType = FlightHandler.getCurrentFlightType(player);
//
//            if (flightType.isNormal()) {
//                float flightAnimation = extension.palladium_getFlightAnimation(partialTicks);
//                float leaning = Mth.clamp(flightAnimation, 0, 20) / 20F;
//                float powered = Mth.clamp(flightAnimation - 20, 0, 10) / 10F;
//                float anim1 = AnimationUtil.ease(Ease.INOUTSINE, leaning);
//                float anim2 = AnimationUtil.ease(Ease.INOUTSINE, powered);
//
//                var body = builder.get(PlayerModelPart.BODY)
//                        .rotateXDegrees(-90.0F - player.getXRot())
//                        .animate(Ease.INBACK, leaning);
//
//                builder.get(PlayerModelPart.HEAD)
//                        .rotateXDegrees(-90.0F)
//                        .animate(Ease.INBACK, leaning);
//
//                var rightArm = builder.get(PlayerModelPart.RIGHT_ARM)
//                        .rotateXDegrees(-180 * anim2)
//                        .rotateYDegrees(20 * anim1 - 20 * anim2)
//                        .rotateZDegrees(5 * anim1 - 17.5F * anim2);
//
//                var leftArm = builder.get(PlayerModelPart.LEFT_ARM)
//                        .rotateX(0)
//                        .rotateYDegrees(-20 * anim1 + 20 * anim2)
//                        .rotateZDegrees(-5 * anim1 - 5 * anim2);
//
//                var rightLeg = builder.get(PlayerModelPart.RIGHT_LEG)
//                        .rotateXDegrees(-17.5F * anim2)
//                        .rotateYDegrees(-5 * anim1)
//                        .rotateZDegrees(2 * anim1 + 3 * anim2);
//
//                if (anim2 > 0F) {
//                    rightLeg.translateY(9F * anim2)
//                            .translateZ(-2F * anim2);
//                }
//
//                var leftLeg = builder.get(PlayerModelPart.LEFT_LEG)
//                        .rotateX(0)
//                        .rotateYDegrees(5 * anim1)
//                        .rotateZDegrees(-2 * anim1 - 3 * anim2);
//
//            } else if (flightType.isLevitation() || flightType.isJetpack()) {
//                var prevMove = extension.palladium_getPrevMovementDelta();
//                var interpolated = prevMove.add(player.getDeltaMovement().subtract(prevMove).scale(partialTicks));
//                float speed = (float) Mth.clamp(Math.sqrt(interpolated.x * interpolated.x + interpolated.z * interpolated.z), 0F, 1F);
//
//                if (speed != 0F) {
//                    builder.get(PlayerModelPart.BODY)
//                            .rotateX(-1)
//                            .animate(Ease.INOUTQUINT, speed);
//
//                    builder.get(PlayerModelPart.RIGHT_ARM)
//                            .rotateX(0)
//                            .rotateZ((float) Math.toRadians(10))
//                            .animate(Ease.INOUTQUINT, speed);
//                    builder.get(PlayerModelPart.LEFT_ARM)
//                            .rotateX(0)
//                            .rotateZ((float) Math.toRadians(-10))
//                            .animate(Ease.INOUTQUINT, speed);
//
//                    builder.get(PlayerModelPart.RIGHT_LEG)
//                            .rotateX(0)
//                            .rotateZ((float) Math.toRadians(5))
//                            .animate(Ease.INOUTQUINT, speed);
//                    builder.get(PlayerModelPart.LEFT_LEG)
//                            .rotateX(0)
//                            .rotateZ((float) Math.toRadians(-5))
//                            .animate(Ease.INOUTQUINT, speed);
//
//                    int type = (player.isOnGround() || player.getAbilities().flying ? 0 : player.zza < 0 ? -1 : 1);
//                    speed *= type;
//                    builder.get(PlayerModelPart.HEAD)
//                            .rotateX((float) (model.head.xRot - 0.52359877559829887307710723054658 * 2))
//                            .animate(Ease.INOUTQUINT, speed);
//
//                }
//            }
//        }
    }
}
