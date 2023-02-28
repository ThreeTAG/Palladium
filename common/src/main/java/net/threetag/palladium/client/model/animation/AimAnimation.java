package net.threetag.palladium.client.model.animation;

import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.threetag.palladium.power.ability.AimAbility;

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
                        .setX(-3.5F / 16F)
                        .setY(1.5F / 16F)
                        .setZ(1.5F / 16F)
                        .setXRot((float) -Math.toRadians(20))
                        .setYRot((float) -Math.toRadians(27))
                        .setZRot((float) -Math.toRadians(30))
                        .animate(Ease.INOUTSINE, right);
            } else {
                builder.get(PlayerModelPart.RIGHT_ARM)
                        .setXRot((float) (model.head.xRot - Math.PI / 2F))
                        .setYRot(model.head.yRot)
                        .setZRot(model.head.zRot)
                        .animate(Ease.INOUTSINE, right);
            }
        }

        if (left > 0) {
            if (firstPersonContext.firstPerson()) {
                builder.get(PlayerModelPart.LEFT_ARM)
                        .setX(3.5F / 16F)
                        .setY(1.5F / 16F)
                        .setZ(1.5F / 16F)
                        .setXRot((float) -Math.toRadians(20))
                        .setYRot((float) Math.toRadians(27))
                        .setZRot((float) Math.toRadians(30))
                        .animate(Ease.INOUTSINE, left);
            } else {
                builder.get(PlayerModelPart.LEFT_ARM)
                        .setXRot((float) (model.head.xRot - Math.PI / 2F))
                        .setYRot(model.head.yRot)
                        .setZRot(model.head.zRot)
                        .animate(Ease.INOUTSINE, left);
            }
        }
    }
}
