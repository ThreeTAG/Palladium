package net.threetag.palladium.client.energybeam;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.PerspectiveValue;
import net.threetag.palladium.util.SizeUtil;
import net.threetag.palladium.util.context.DataContext;
import org.joml.Vector2f;

public class LaserBeamRenderer extends EnergyBeamRenderer {

    private final LaserRenderer laserRenderer;
    private final PerspectiveValue<Boolean> visibility;

    public LaserBeamRenderer(LaserRenderer laserRenderer, PerspectiveValue<Boolean> visibility) {
        this.laserRenderer = laserRenderer;
        this.visibility = visibility;
    }

    @Override
    public void render(AbstractClientPlayer player, Vec3 origin, Vec3 target, float lengthMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        if (this.visibility.getForPlayer(player)) {
            var size = this.laserRenderer.getSize();

            this.laserRenderer
                    .size(size.mul(SizeUtil.getInstance().getModelWidthScale(player, partialTick), SizeUtil.getInstance().getModelHeightScale(player, partialTick), new Vector2f()))
                    .length((float) origin.distanceTo(target) * lengthMultiplier)
                    .faceAndRender(DataContext.forEntity(player), poseStack, bufferSource, origin, target, player.tickCount, partialTick);

            this.laserRenderer.size(size);
        }
    }

    public static class Serializer extends EnergyBeamRenderer.Serializer {

        public static final ResourceLocation ID = Palladium.id("laser");

        @Override
        public EnergyBeamRenderer fromJson(JsonObject json) {
            return new LaserBeamRenderer(LaserRenderer.fromJson(json), PerspectiveValue.getFromJson(json, "visibility", JsonElement::getAsBoolean, true));
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Laser");
            LaserRenderer.generateDocumentation(builder, 2, false);

            var exampleJson = new JsonObject();
            exampleJson.addProperty("first_person", true);
            exampleJson.addProperty("third_person", true);
            builder.addProperty("visibility", Boolean.class)
                    .description("Determines if its visible.")
                    .fallback(true).exampleJson(exampleJson);
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }
}
