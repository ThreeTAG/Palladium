package net.threetag.palladium.client.model.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.util.Easing;

@Environment(EnvType.CLIENT)
public class LevitationAnimation extends PalladiumAnimation {

    public LevitationAnimation(int priority) {
        super(priority);
    }

    @Override
    public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
        boolean active = !firstPersonContext.firstPerson();

        if (active && player instanceof PalladiumPlayerExtension extension) {
            var flight = extension.palladium$getFlightHandler();
            float anim = flight.getLevitationAnimation(partialTicks);

            if (anim <= 0F) {
                return;
            }

            builder.get(PlayerModelPart.BODY)
                    .setXRotShortestDegrees(-15F)
                    .animate(Easing.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.HEAD)
                    .setXRotShortestDegrees(-15.0F)
                    .animate(Easing.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.RIGHT_ARM)
                    .setXRotShortestDegrees(0F)
                    .setYRotShortestDegrees(0F)
                    .setZRotShortestDegrees(10F)
                    .animate(Easing.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.LEFT_ARM)
                    .setXRotShortestDegrees(0F)
                    .setYRotShortestDegrees(0F)
                    .setZRotShortestDegrees(-10F)
                    .animate(Easing.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.RIGHT_LEG)
                    .resetX()
                    .resetY()
                    .resetZ()
                    .setXRotShortestDegrees(10F)
                    .setYRotShortestDegrees(0F)
                    .setZRotShortestDegrees(2.5F)
                    .animate(Easing.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.LEFT_LEG)
                    .resetX()
                    .resetY()
                    .resetZ()
                    .setXRotShortestDegrees(5F)
                    .setYRotShortestDegrees(0F)
                    .setZRotShortestDegrees(-2.5F)
                    .animate(Easing.INOUTCUBIC, anim);
        }
    }
}
