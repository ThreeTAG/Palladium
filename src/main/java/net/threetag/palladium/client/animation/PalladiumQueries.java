package net.threetag.palladium.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.neoforge.event.MolangEvent;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.SwingingFlightType;

public class PalladiumQueries {

    public static void registerQueries(MolangEvent e) {
        e.setDoubleQuery("age", actor -> {
            return ((PlayerAnimationController) actor).getAvatar().tickCount + actor.getAnimationData().getPartialTick();
        });
        e.setDoubleQuery("velocity", actor -> {
           return actor.getAnimationData().getVelocity();
        });
        e.setDoubleQuery("flight_pitch", actor -> {
            var animation = EntityFlightHandler.get(((PlayerAnimationController) actor).getAvatar()).getAnimationHandler();
            return animation != null ? animation.getPitch(actor.getAnimationData().getPartialTick()) : 0;
        });
        e.setDoubleQuery("flight_roll", actor -> {
            var animation = EntityFlightHandler.get(((PlayerAnimationController) actor).getAvatar()).getAnimationHandler();
            return animation != null ? animation.getRoll(actor.getAnimationData().getPartialTick()) : 0;
        });
        e.setDoubleQuery("flight_yaw", actor -> {
            var animation = EntityFlightHandler.get(((PlayerAnimationController) actor).getAvatar()).getAnimationHandler();
            return animation != null ? animation.getYaw(actor.getAnimationData().getPartialTick()) : 0;
        });
        e.setDoubleQuery("flight_limb_pitch", actor -> {
            var animation = EntityFlightHandler.get(((PlayerAnimationController) actor).getAvatar()).getAnimationHandler();
            return animation != null ? animation.getLimbPitch(actor.getAnimationData().getPartialTick()) : 0;
        });
        e.setDoubleQuery("flight_limb_roll", actor -> {
            var animation = EntityFlightHandler.get(((PlayerAnimationController) actor).getAvatar()).getAnimationHandler();
            return animation != null ? animation.getLimbRoll(actor.getAnimationData().getPartialTick()) : 0;
        });
        e.setDoubleQuery("flight_limb_yaw", actor -> {
            var animation = EntityFlightHandler.get(((PlayerAnimationController) actor).getAvatar()).getAnimationHandler();
            return animation != null ? animation.getLimbYaw(actor.getAnimationData().getPartialTick()) : 0;
        });
        e.setDoubleQuery("swinging_right_arm_pitch", actor -> {
            var animation = EntityFlightHandler.get(((PlayerAnimationController) actor).getAvatar()).getAnimationHandler();
            return animation instanceof SwingingFlightType.AnimationHandler ? ((SwingingFlightType.AnimationHandler) animation).getRightArmPitch(actor.getAnimationData().getPartialTick()) : 0;
        });
        e.setDoubleQuery("swinging_left_arm_pitch", actor -> {
            var animation = EntityFlightHandler.get(((PlayerAnimationController) actor).getAvatar()).getAnimationHandler();
            return animation instanceof SwingingFlightType.AnimationHandler ? ((SwingingFlightType.AnimationHandler) animation).getLeftArmPitch(actor.getAnimationData().getPartialTick()) : 0;
        });
    }
}
