package net.threetag.palladium.client.model.animation;

import dev.kosmx.playerAnim.core.util.Ease;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.threetag.palladium.entity.PalladiumPlayerExtension;

@Environment(EnvType.CLIENT)
public class FlightAnimation extends PalladiumAnimation {

    public FlightAnimation(int priority) {
        super(priority);
    }

    @Override
    public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
        boolean active = !firstPersonContext.firstPerson();

        if (active && player instanceof PalladiumPlayerExtension extension) {
            float anim = extension.palladium_getFlightAnimation(partialTicks);

            if (anim <= 1F) {
                return;
            }

            anim = (anim - 1F) / 2F;

            builder.get(PlayerModelPart.BODY)
                    .setXRotDegrees(-90.0F - player.getXRot())
                    .setY(player.getBbHeight() / 2F * 16F)
                    .setZ(player.getBbHeight() / 2F * 16F)
                    .animate(Ease.INOUTCUBIC, anim);

            builder.get(PlayerModelPart.HEAD)
                    .setXRotDegrees(-90.0F)
                    .animate(Ease.INOUTCUBIC, anim);
        }
    }
}
