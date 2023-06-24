package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.model.animation.FlightAnimation;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.util.Easing;

public class CapeTextureVariable extends AbstractFloatTextureVariable {

    public final boolean bobbing;

    public CapeTextureVariable(JsonObject json) {
        super(json);
        this.bobbing = GsonHelper.getAsBoolean(json, "bobbing", true);
    }

    @Override
    public float getNumber(Entity entity) {
        if (entity instanceof Player player) {
            double d0 = Mth.lerp(1F, player.xCloakO, player.xCloak) - Mth.lerp(1F, entity.xo, entity.getX());
            double d1 = Mth.lerp(1F, player.yCloakO, player.yCloak) - Mth.lerp(1F, entity.yo, entity.getY());
            double d2 = Mth.lerp(1F, player.zCloakO, player.zCloak) - Mth.lerp(1F, entity.zo, entity.getZ());
            float f = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO);
            double d3 = Mth.sin(f * ((float) Math.PI / 180F));
            double d4 = -Mth.cos(f * ((float) Math.PI / 180F));
            float f1 = (float) d1 * 10.0F;
            f1 = Mth.clamp(f1, -6.0F, 32.0F);
            float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            f2 = Mth.clamp(f2, 0.0F, 150.0F);
            if (f2 < 0.0F) {
                f2 = 0.0F;
            }

            if (this.bobbing) {
                float f4 = Mth.lerp(1F, player.oBob, player.bob);
                f1 = f1 + Mth.sin(Mth.lerp(1F, entity.walkDistO, entity.walkDist) * 6.0F) * 32.0F * f4;
            }

            float rotation = 6.0F + f2 / 2.0F + f1;
            float val = Mth.clamp(rotation + 10F, 0F, 90F);

            if (player instanceof PalladiumPlayerExtension extension) {
                float hoveringAnimation = extension.palladium_getHoveringAnimation(1F) - extension.palladium_getLevitationAnimation(1F);

                if (hoveringAnimation > 0F) {
                    val += (20F - val) * hoveringAnimation;
                }

                float prevFlightAnimation = extension.palladium_getFlightAnimation(0F);
                float flightAnimation = extension.palladium_getFlightAnimation(1F);

                if (flightAnimation <= 1F) {
                    return val;
                }

                flightAnimation = (flightAnimation - 1F) / 2F;
                prevFlightAnimation = (prevFlightAnimation - 1F) / 2F;
                float dest = 10;

                if (prevFlightAnimation > flightAnimation) {
                    dest = (1F - Mth.clamp(flightAnimation * 4F - 3F, 0F, 1F)) * 70F;
                    flightAnimation = Mth.clamp(flightAnimation * 3, 0F, 1F);
                }

                var vec1 = FlightAnimation.to2D(extension.palladium_getFlightVector(1F));
                var vec2 = FlightAnimation.to2D(extension.palladium_getLookAngle(1F));
                var tilt = Mth.clamp(FlightAnimation.angleBetweenVector(vec1, vec2), -0.5F, 0.5F) * 90F * extension.palladium_getHorizontalSpeed(1F);

                if (tilt != 0F) {
                    dest = Mth.abs((float) tilt);
                }

                val += (dest - val) * Easing.INOUTCIRC.apply(flightAnimation);
            }

            return val;
        }

        return 0F;
    }
}