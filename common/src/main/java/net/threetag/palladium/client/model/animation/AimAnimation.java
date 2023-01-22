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
                        .translateX(-3.5F / 16F)
                        .translateY(1.5F / 16F)
                        .translateZ(1.5F / 16F)
                        .rotateX((float) -Math.toRadians(20))
                        .rotateY((float) -Math.toRadians(27))
                        .rotateZ((float) -Math.toRadians(30))
                        .animate(Ease.INOUTSINE, right);
            } else {
                builder.get(PlayerModelPart.RIGHT_ARM)
                        .rotateX((float) (model.head.xRot - Math.PI / 2F))
                        .rotateY(model.head.yRot)
                        .rotateZ(model.head.zRot)
                        .animate(Ease.INOUTSINE, right);
            }
        }

        if (left > 0) {
            if (firstPersonContext.firstPerson()) {
                builder.get(PlayerModelPart.LEFT_ARM)
                        .translateX(3.5F / 16F)
                        .translateY(1.5F / 16F)
                        .translateZ(1.5F / 16F)
                        .rotateX((float) -Math.toRadians(20))
                        .rotateY((float) Math.toRadians(27))
                        .rotateZ((float) Math.toRadians(30))
                        .animate(Ease.INOUTSINE, left);
            } else {
                builder.get(PlayerModelPart.LEFT_ARM)
                        .rotateX((float) (model.head.xRot - Math.PI / 2F))
                        .rotateY(model.head.yRot)
                        .rotateZ(model.head.zRot)
                        .animate(Ease.INOUTSINE, left);
            }
        }
    }
}
