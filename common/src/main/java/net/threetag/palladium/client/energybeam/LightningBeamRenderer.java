package net.threetag.palladium.client.energybeam;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.PerspectiveValue;
import net.threetag.palladium.util.SizeUtil;
import net.threetag.palladium.util.json.GsonUtil;
import org.joml.Vector2f;

public class LightningBeamRenderer extends EnergyBeamRenderer {

    private final LaserRenderer laserRenderer;
    private final int segments;
    private final int frequency;
    private final float spread;
    private final PerspectiveValue<Boolean> visibility;

    public LightningBeamRenderer(LaserRenderer laserRenderer, int segments, int frequency, float spread, PerspectiveValue<Boolean> visibility) {
        this.laserRenderer = laserRenderer;
        this.segments = segments;
        this.frequency = frequency;
        this.spread = spread;
        this.visibility = visibility;
    }

    @Override
    public void render(AbstractClientPlayer player, Vec3 origin, Vec3 target, float lengthMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, boolean isFirstPerson, float partialTick) {
        if (this.visibility.getForPlayer(player)) {
            var size = this.laserRenderer.getSize();
            var widthScale = SizeUtil.getInstance().getModelWidthScale(player, partialTick);
            var heightScale = SizeUtil.getInstance().getModelHeightScale(player, partialTick);
            this.laserRenderer
                    .size(size.mul(widthScale, heightScale, new Vector2f()));
            var segmentPartVec = target.subtract(origin).scale(1F / this.segments);
            var randomStart = RandomSource.create(player.getId() + (player.tickCount / this.frequency));
            var startVec = origin;

            for (int i = 0; i < this.segments; i++) {
                var startProgress = (1F / this.segments) * i;
                var endProgress = (1F / this.segments) * (i + 1);
                var currentProgress = lengthMultiplier <= startProgress ? 0F : (lengthMultiplier >= endProgress ? 1F : (lengthMultiplier - startProgress) / (endProgress - startProgress));

                if (currentProgress > 0F) {
                    var end = i == this.segments - 1 ? target : origin.add(segmentPartVec.scale(i + 1)).add(randomizeVector(randomStart, this.spread));
                    var offset = startVec.subtract(origin);

                    poseStack.pushPose();
                    poseStack.translate(offset.x, offset.y, offset.z);
                    this.laserRenderer.length((float) startVec.distanceTo(end) * currentProgress)
                            .faceAndRender(poseStack, bufferSource, startVec, end, player.tickCount, partialTick);
                    poseStack.popPose();
                    startVec = end;
                }
            }

            this.laserRenderer.size(size);
        }
    }

    private static Vec3 randomizeVector(RandomSource random, float spread) {
        return new Vec3((random.nextFloat() - 0.5F) * 2F * spread, (random.nextFloat() - 0.5F) * 2F * spread, (random.nextFloat() - 0.5F) * 2F * spread);
    }

    public static class Serializer extends EnergyBeamRenderer.Serializer {

        public static final ResourceLocation ID = Palladium.id("lightning");

        @Override
        public EnergyBeamRenderer fromJson(JsonObject json) {
            return new LightningBeamRenderer(
                    LaserRenderer.fromJson(json, 1),
                    GsonUtil.getAsIntMin(json, "segments", 1, 5),
                    GsonUtil.getAsIntMin(json, "frequency", 1, 2),
                    GsonUtil.getAsFloatMin(json, "spread", 0, 5) / 16F,
                    PerspectiveValue.getFromJson(json, "visibility", JsonElement::getAsBoolean, true)
            );
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("Lightning");

            builder.addProperty("segments", Integer.class)
                    .description("Determines how many split segments the lightning will have").fallback(5).exampleJson(new JsonPrimitive(5));
            builder.addProperty("frequency", Integer.class)
                    .description("Determines how many ticks it takes for the beam to change its offset position").fallback(2).exampleJson(new JsonPrimitive(2));
            builder.addProperty("spread", Float.class)
                    .description("Determines how far each offset segment-corner spreads from the center").fallback(5F).exampleJson(new JsonPrimitive(5));

            LaserRenderer.generateDocumentation(builder, 1, false);

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
