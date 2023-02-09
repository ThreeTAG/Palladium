package net.threetag.palladium.client.model.animation;

import dev.kosmx.playerAnim.core.util.Ease;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.threetag.palladium.entity.PalladiumPlayerExtension;

@Environment(EnvType.CLIENT)
public class LevitationAnimation extends PalladiumAnimation {

    public LevitationAnimation(int priority) {
        super(priority);
    }

    @Override
    public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
        boolean active = !firstPersonContext.firstPerson();

        if (active && player instanceof PalladiumPlayerExtension extension) {
            float anim = Math.min(extension.palladium_getFlightAnimation(partialTicks), 1F);

            if (anim <= 0F) {
                return;
            }

            builder.get(PlayerModelPart.BODY)
                    .setXRotDegrees(-15F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.HEAD)
                    .setXRotDegrees(-15.0F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.RIGHT_ARM)
                    .setXRotDegrees(7.5F)
                    .setYRotDegrees(10F)
                    .setZRotDegrees(7.5F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.LEFT_ARM)
                    .setXRotDegrees(7.5F)
                    .setYRotDegrees(-10F)
                    .setZRotDegrees(-7.5F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.RIGHT_LEG)
                    .setXRotDegrees(2.5F)
                    .setYRotDegrees(5F)
                    .setZRotDegrees(2.5F)
                    .resetX()
                    .resetY()
                    .resetZ()
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.LEFT_LEG)
                    .setXRotDegrees(2.5F)
                    .setYRotDegrees(-5F)
                    .setZRotDegrees(-2.5F)
                    .resetX()
                    .resetY()
                    .resetZ()
                    .animate(Ease.INOUTCUBIC, anim);
        }
    }
}
