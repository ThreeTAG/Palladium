package net.threetag.palladium.client.beam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import org.joml.Vector2f;

import java.awt.*;

public class LaserBeamRenderer extends BeamRenderer {

    public static final MapCodec<LaserBeamRenderer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LaserRenderer.codec(2).fieldOf("render_settings").forGetter(beam -> beam.laserRenderer)
    ).apply(instance, LaserBeamRenderer::new));

    private final LaserRenderer laserRenderer;

    public LaserBeamRenderer(LaserRenderer laserRenderer) {
        this.laserRenderer = laserRenderer;
    }

    @Override
    public void submit(DataContext context, Vec3 origin, Vec3 target, Vec2 sizeMultiplier, float lengthMultiplier, float opacityMultiplier, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, int ageInTicks, float partialTick) {
        this.laserRenderer.faceAndRender(poseStack, submitNodeCollector, origin, target, ageInTicks, partialTick, lengthMultiplier, opacityMultiplier, sizeMultiplier);
    }

    @Override
    public BeamRendererSerializer<?> getSerializer() {
        return BeamRendererSerializers.LASER;
    }

    public static class Serializer extends BeamRendererSerializer<LaserBeamRenderer> {

        @Override
        public MapCodec<LaserBeamRenderer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<BeamRenderer, LaserBeamRenderer> builder, HolderLookup.Provider provider) {
            builder.setName("Laser")
                    .setDescription("Renders a laser beam between two points.")
                    .add("render_settings", TYPE_LASER_RENDERER, "The render settings for the laser.")
                    .addExampleObject(new LaserBeamRenderer(
                            new LaserRenderer(
                                    new LaserRenderer.LaserPart(Color.BLUE, 1F, 0F, null),
                                    new LaserRenderer.LaserPart(Color.WHITE, 1F, 0F, null),
                                    2, new Vector2f(2 / 16F, 2 / 16F), 0, 0
                            )
                    ));
        }
    }
}
