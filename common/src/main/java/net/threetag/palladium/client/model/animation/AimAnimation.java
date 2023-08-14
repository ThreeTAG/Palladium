package net.threetag.palladium.client.model.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.threetag.palladium.power.ability.AimAbility;
import net.threetag.palladium.util.Easing;

public class AimAnimation extends PalladiumAnimation {


    public AimAnimation(int priority) {
        super(priority);
    }

    @Override
    public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
        var right = AimAbility.getTimer(player, partialTicks, true);
        var left = AimAbility.getTimer(player, partialTicks, false);

        if (right > 0F) {
            if (firstPersonContext.firstPerson()) {
                builder.get(PlayerModelPart.RIGHT_ARM)
                        .setY(-1).setX(-5).setZ(4)
                        .setXRotDegrees(-8.5F).setYRotDegrees(-32.5F).setZRotDegrees(-33F)
                        .animate(Easing.INOUTSINE, right);
            } else {
                builder.get(PlayerModelPart.RIGHT_ARM)
                        .setXRot((float) (model.head.xRot - Math.PI / 2F))
                        .setYRot(model.head.yRot)
                        .setZRot(model.head.zRot)
                        .animate(Easing.INOUTSINE, right);
            }
        }

        if (left > 0) {
            if (firstPersonContext.firstPerson()) {
                builder.get(PlayerModelPart.LEFT_ARM)
                        .setY(-1).setX(5).setZ(4)
                        .setXRotDegrees(-8.5F).setYRotDegrees(32.5F).setZRotDegrees(33F)
                        .animate(Easing.INOUTSINE, left);
            } else {
                builder.get(PlayerModelPart.LEFT_ARM)
                        .setXRot((float) (model.head.xRot - Math.PI / 2F))
                        .setYRot(model.head.yRot)
                        .setZRot(model.head.zRot)
                        .animate(Easing.INOUTSINE, left);
            }
        }
    }
}
