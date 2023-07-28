package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.model.animation.FlightAnimation;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.util.Easing;
import net.threetag.palladium.util.context.DataContext;

import java.util.List;

public class CapeTextureVariable extends AbstractFloatTextureVariable {

    public final boolean bobbing;

    public CapeTextureVariable(boolean bobbing, List<Pair<Operation, JsonPrimitive>> operations) {
        super(operations);
        this.bobbing = bobbing;
    }

    @Override
    public float getNumber(DataContext context) {
        var player = context.getPlayer();
        if (player != null) {
            double d0 = Mth.lerp(1F, player.xCloakO, player.xCloak) - Mth.lerp(1F, player.xo, player.getX());
            double d1 = Mth.lerp(1F, player.yCloakO, player.yCloak) - Mth.lerp(1F, player.yo, player.getY());
            double d2 = Mth.lerp(1F, player.zCloakO, player.zCloak) - Mth.lerp(1F, player.zo, player.getZ());
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
                f1 = f1 + Mth.sin(Mth.lerp(1F, player.walkDistO, player.walkDist) * 6.0F) * 32.0F * f4;
            }

            float rotation = 6.0F + f2 / 2.0F + f1;
            float val = Mth.clamp(rotation + 10F, 0F, 90F);

            if (player instanceof PalladiumPlayerExtension extension) {
                var flight = extension.palladium$getFlightHandler();
                float hoveringAnimation = flight.getHoveringAnimation(1F) - flight.getLevitationAnimation(1F);

                if (hoveringAnimation > 0F) {
                    val += (20F - val) * hoveringAnimation;
                }

                float prevFlightAnimation = flight.getFlightAnimation(0F);
                float flightAnimation = flight.getFlightAnimation(1F);

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

                var vec1 = FlightAnimation.to2D(flight.getFlightVector(1F));
                var vec2 = FlightAnimation.to2D(flight.getLookAngle(1F));
                var tilt = Mth.clamp(FlightAnimation.angleBetweenVector(vec1, vec2), -0.5F, 0.5F) * 90F * flight.getHorizontalSpeed(1F);

                if (tilt != 0F) {
                    dest = Mth.abs((float) tilt);
                }

                val += (dest - val) * Easing.INOUTCIRC.apply(flightAnimation);
            }

            return val;
        }

        return 0F;
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new CapeTextureVariable(
                    GsonHelper.getAsBoolean(json, "bobbing", true),
                    AbstractFloatTextureVariable.parseOperations(json));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the tilt of the player's cape.";
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Cape");
            
            builder.addProperty("bobbing", Boolean.class)
                    .description("Determines of bobbing should be taken into account when doing the calculation")
                    .fallback(true).exampleJson(new JsonPrimitive(true));

            AbstractFloatTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("cape");
        }
    }
}