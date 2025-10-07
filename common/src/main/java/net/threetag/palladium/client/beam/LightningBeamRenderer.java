package net.threetag.palladium.client.beam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PalladiumCodecs;
import org.joml.Vector2f;

import java.awt.*;

public class LightningBeamRenderer extends BeamRenderer {

    public static final MapCodec<LightningBeamRenderer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LaserRenderer.codec(1).fieldOf("render_settings").forGetter(beam -> beam.laserRenderer),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("segments", 5).forGetter(beam -> beam.segments),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("frequency", 2).forGetter(beam -> beam.frequency),
            PalladiumCodecs.NON_NEGATIVE_VOXEL_FLOAT.optionalFieldOf("spread", 5F).forGetter(beam -> beam.spread)
    ).apply(instance, LightningBeamRenderer::new));

    private final LaserRenderer laserRenderer;
    private final int segments;
    private final int frequency;
    private final float spread;

    public LightningBeamRenderer(LaserRenderer laserRenderer, int segments, int frequency, float spread) {
        this.laserRenderer = laserRenderer;
        this.segments = segments;
        this.frequency = frequency;
        this.spread = spread;
    }

    @Override
    public void render(Vec3 origin, Vec3 target, Vec2 sizeMultiplier, float lengthMultiplier, float opacityMultiplier, PoseStack poseStack, MultiBufferSource bufferSource, int packedLightIn, int ageInTicks, float partialTick) {
        var segmentPartVec = target.subtract(origin).scale(1F / this.segments);
        var randomStart = RandomSource.create(ageInTicks / this.frequency);
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
                this.laserRenderer
                        .faceAndRender(poseStack, bufferSource, startVec, end, ageInTicks, partialTick, currentProgress, opacityMultiplier, sizeMultiplier);
                poseStack.popPose();
                startVec = end;
            }
        }
    }

    private static Vec3 randomizeVector(RandomSource random, float spread) {
        return new Vec3((random.nextFloat() - 0.5F) * 2F * spread, (random.nextFloat() - 0.5F) * 2F * spread, (random.nextFloat() - 0.5F) * 2F * spread);
    }

    @Override
    public BeamRendererSerializer<?> getSerializer() {
        return BeamRendererSerializers.LIGHTNING;
    }

    public static class Serializer extends BeamRendererSerializer<LightningBeamRenderer> {

        @Override
        public MapCodec<LightningBeamRenderer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<BeamRenderer, LightningBeamRenderer> builder, HolderLookup.Provider provider) {
            builder.setName("Lightning Beam")
                    .setDescription("Renders a fluctuating lightning between two points.")
                    .add("render_settings", TYPE_LASER_RENDERER, "The render settings for the lightning.")
                    .addOptional("segments", TYPE_INT, "The amount of segments the lightning should have.", 5)
                    .addOptional("frequency", TYPE_INT, "The frequency of the lightning fluctuation.", 2)
                    .addOptional("spread", TYPE_FLOAT, "The spread of the lightning fluctuation. It defines how \"far\" the lightning can spread out from the core", 5F)
                    .setExampleObject(new LightningBeamRenderer(
                            new LaserRenderer(
                                    new LaserRenderer.LaserPart(Color.BLUE, 1F, 0F, null),
                                    new LaserRenderer.LaserPart(Color.WHITE, 1F, 0F, null),
                                    2, new Vector2f(2 / 16F, 2 / 16F), 0, 0
                            ),
                            10,
                            4,
                            7F
                    ));
        }
    }
}
