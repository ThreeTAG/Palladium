package net.threetag.palladium.client.trail;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ARGB;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Unit;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.util.PalladiumCodecs;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GradientTrailRenderer extends TrailRenderer<Unit> {

    public static MapCodec<GradientTrailRenderer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PalladiumCodecs.COLOR_CODEC.optionalFieldOf("color", Color.WHITE).forGetter(r -> r.color),
            PalladiumCodecs.FLOAT_0_TO_1.optionalFieldOf("opacity", 0.5F).forGetter(r -> r.opacity),
            Orientation.CODEC.optionalFieldOf("orientation", Orientation.VERTICAL).forGetter(r -> r.orientation),
            PalladiumCodecs.FLOAT_0_TO_1.optionalFieldOf("offset", 0.5F).forGetter(r -> r.offset)
    ).apply(instance, GradientTrailRenderer::new));

    private final Color color;
    private final float opacity;
    private final Orientation orientation;
    private final float offset;

    public GradientTrailRenderer(Color color, float opacity, Orientation orientation, float offset) {
        this.color = color;
        this.opacity = opacity;
        this.orientation = orientation;
        this.offset = offset;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LivingEntityRenderState ownerRenderState, TrailSegment<Unit> trailSegment, int packedLight, float partialTick) {
        submitNodeCollector.submitCustomGeometry(poseStack, PalladiumRenderTypes.LASER, (pose, vertexConsumer) -> {
            var nextSegment = trailSegment.getNextSegment();
            var nextPos = (nextSegment != null ? nextSegment.getPosition() : new Vec3(ownerRenderState.x, ownerRenderState.y, ownerRenderState.z)).subtract(trailSegment.getPosition());
            var nextHeight = nextSegment != null ? nextSegment.getDimensions().height() : ownerRenderState.boundingBoxHeight;
            var nextWidth = nextSegment != null ? nextSegment.getDimensions().width() : ownerRenderState.boundingBoxWidth;
            var nextYRot = nextSegment != null ? nextSegment.getYRot() : ownerRenderState.bodyRot;
            var color = ARGB.color(this.opacity * trailSegment.getOpacity(), this.color.getRGB());
            var nextColor = ARGB.color(this.opacity * (nextSegment != null ? nextSegment.getOpacity() : 1F), this.color.getRGB());

            if (this.orientation == Orientation.VERTICAL) {
                var halfWidth = trailSegment.getDimensions().width() / 2F;
                var offset = trailSegment.getDimensions().width() * this.offset;
                var pos1 = new Vec3(-halfWidth + offset, 0, 0).yRot((float) Math.toRadians(-trailSegment.getYRot()));
                var pos2 = new Vec3(-halfWidth + offset, trailSegment.getDimensions().height(), 0).yRot((float) Math.toRadians(-trailSegment.getYRot()));

                var nextHalfWidth = nextWidth / 2F;
                var nextOffset = nextWidth * this.offset;
                var nextPos1 = nextPos.add(new Vec3(-nextHalfWidth + nextOffset, 0, 0).yRot((float) Math.toRadians(-nextYRot)));
                var nextPos2 = nextPos.add(new Vec3(-nextHalfWidth + nextOffset, nextHeight, 0).yRot((float) Math.toRadians(-nextYRot)));

                vertexConsumer.addVertex(pose, pos1.toVector3f()).setColor(color).setLight(packedLight);
                vertexConsumer.addVertex(pose, pos2.toVector3f()).setColor(color).setLight(packedLight);
                vertexConsumer.addVertex(pose, nextPos2.toVector3f()).setColor(nextColor).setLight(packedLight);
                vertexConsumer.addVertex(pose, nextPos1.toVector3f()).setColor(nextColor).setLight(packedLight);
            } else {
                var offset = trailSegment.getDimensions().height() * this.offset;
                var pos1 = new Vec3(trailSegment.getDimensions().width() / 2F, offset, 0).yRot((float) Math.toRadians(-trailSegment.getYRot()));
                var pos2 = new Vec3(trailSegment.getDimensions().width() / -2F, offset, 0).yRot((float) Math.toRadians(-trailSegment.getYRot()));

                var nextOffset = nextHeight * this.offset;
                var nextPos1 = nextPos.add(new Vec3(nextWidth / 2F, nextOffset, 0).yRot((float) Math.toRadians(-nextYRot)));
                var nextPos2 = nextPos.add(new Vec3(nextWidth / -2F, nextOffset, 0).yRot((float) Math.toRadians(-nextYRot)));

                vertexConsumer.addVertex(pose, pos1.toVector3f()).setColor(color).setLight(packedLight);
                vertexConsumer.addVertex(pose, pos2.toVector3f()).setColor(color).setLight(packedLight);
                vertexConsumer.addVertex(pose, nextPos2.toVector3f()).setColor(nextColor).setLight(packedLight);
                vertexConsumer.addVertex(pose, nextPos1.toVector3f()).setColor(nextColor).setLight(packedLight);
            }
        });
    }

    @Override
    public TrailRendererSerializer<?> getSerializer() {
        return TrailRendererSerializers.GRADIENT;
    }

    public static class Serializer extends TrailRendererSerializer<GradientTrailRenderer> {

        @Override
        public MapCodec<GradientTrailRenderer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<TrailRenderer<?>, GradientTrailRenderer> builder, HolderLookup.Provider provider) {
            builder.setName("Gradient Trail").setDescription("Renders a colored gradient behind the players.")
                    .addOptional("color", TYPE_COLOR, "Defines the color of the gradient", "#ffffff")
                    .addOptional("opacity", TYPE_FLOAT, "Starting opacity of the gradient", 0.5F)
                    .addOptional("orientation", SettingType.enumList(Orientation.values()), "Defines the orientation in relation to the entity in which the gradient will render", Orientation.VERTICAL)
                    .addOptional("offset", TYPE_FLOAT, "Defines where the gradient will start in relation to entity hitbox. 0.5 is equal to the center.", 0.5F)
                    .addExampleObject(new GradientTrailRenderer(Color.RED, 0.7F, Orientation.HORIZONTAL, 0.2F));
        }
    }

    public enum Orientation implements StringRepresentable {

        HORIZONTAL("horizontal"),
        VERTICAL("vertical");

        public static final Codec<Orientation> CODEC = StringRepresentable.fromEnum(Orientation::values);

        private final String name;

        Orientation(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
