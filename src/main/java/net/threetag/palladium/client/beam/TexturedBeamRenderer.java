package net.threetag.palladium.client.beam;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.client.renderer.RenderTypeRegistry;
import net.threetag.palladium.client.renderer.RenderTypeFunction;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.MathUtil;
import org.joml.Vector2f;

public class TexturedBeamRenderer extends BeamRenderer {

    public static final MapCodec<TexturedBeamRenderer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TextureReference.CODEC.fieldOf("texture").forGetter(r -> r.texture),
            LaserRenderer.SIZE_CODEC.optionalFieldOf("size", new Vector2f(1 / 16F, 1 / 16F)).forGetter(r -> r.size),
            ExtraCodecs.floatRange(0F, 360F).optionalFieldOf("rotation", 0F).forGetter(r -> r.rotation),
            ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("rotation_speed", 0F).forGetter(r -> r.rotationSpeed),
            RenderTypeRegistry.CODEC.optionalFieldOf("render_type", RenderTypeRegistry.ENTITY_TRANSLUCENT).forGetter(r -> r.renderType),
            ExtraCodecs.intRange(0, 15).optionalFieldOf("light_emission", 0).forGetter(r -> r.lightEmission)
    ).apply(instance, TexturedBeamRenderer::new));

    private final TextureReference texture;
    private final Vector2f size;
    private final float rotation, rotationSpeed;
    private final RenderTypeFunction renderType;
    private final int lightEmission;

    public TexturedBeamRenderer(TextureReference texture, Vector2f size, float rotation, float rotationSpeed, RenderTypeFunction renderType, int lightEmission) {
        this.texture = texture;
        this.size = size;
        this.rotation = rotation;
        this.rotationSpeed = rotationSpeed;
        this.renderType = renderType;
        this.lightEmission = lightEmission;
    }

    @Override
    public void submit(DataContext context, Vec3 origin, Vec3 target, Vec2 sizeMultiplier, float lengthMultiplier, float opacityMultiplier, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, int ageInTicks, float partialTick) {
        var texture = this.texture.getTexture(context);

        if (texture == null) {
            return;
        }

        poseStack.pushPose();
        RenderUtil.faceVec(poseStack, origin, target);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        var rot = this.rotation;

        if (this.rotationSpeed > 0F) {
            rot += (ageInTicks + partialTick) * this.rotationSpeed;
        }

        poseStack.mulPose(Axis.YP.rotationDegrees(rot % 360F));
        var renderType = this.renderType.getRenderType(texture);
        var light = LightTexture.lightCoordsWithEmission(packedLightIn, this.lightEmission);
        var size = new Vector2f(this.size).mul(sizeMultiplier.x, sizeMultiplier.y);
        var length = origin.distanceTo(target) * lengthMultiplier;

        float segmentLength = MathUtil.avg(size.x, size.y);
        int segments = Mth.ceil(length / segmentLength);

        for (int i = 0; i < segments; i++) {
            AABB box = new AABB(-size.x / 2F, i * segmentLength, -size.y / 2F, size.x / 2F, (i + 1) * segmentLength, size.y / 2F);
            var heightMultiplier = i == segments - 1 ? (float) (length - (segmentLength * i)) / segmentLength : 1F;
            RenderUtil.submitTexturedBox(poseStack, renderType, submitNodeCollector, box, RenderUtil.FULL_WHITE, light, OverlayTexture.NO_OVERLAY, heightMultiplier);
        }

        poseStack.popPose();
    }

    @Override
    public BeamRendererSerializer<?> getSerializer() {
        return BeamRendererSerializers.TEXTURED;
    }

    public static class Serializer extends BeamRendererSerializer<TexturedBeamRenderer> {

        @Override
        public MapCodec<TexturedBeamRenderer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<BeamRenderer, TexturedBeamRenderer> builder, HolderLookup.Provider provider) {
            builder.setName("Textured")
                    .setDescription("Renders a beam between two points using a custom texture.")
                    .add("texture", TYPE_TEXTURE_REFERENCE, "The texture to use for the beam.")
                    .addOptional("size", TYPE_FLOAT, "The size/diameter of the beam.", 1F)
                    .addOptional("rotation", TYPE_FLOAT, "The initial rotation of the beam in degrees.", 0F)
                    .addOptional("rotation_speed", TYPE_FLOAT, "The rotation speed of the beam.", 0F)
                    .addOptional("render_type", SettingType.enumList(RenderTypeRegistry.types().stream().map(ResourceLocation::toString).toList()), "The render type to render the beam with.", RenderTypeRegistry.getKey(RenderTypeRegistry.ENTITY_TRANSLUCENT))
                    .addOptional("light_emission", TYPE_INT, "The light emission of the beam. Must be within 0 - 15", 0)
                    .setExampleObject(new TexturedBeamRenderer(
                            TextureReference.normal(ResourceLocation.fromNamespaceAndPath("namespace", "textures/beams/example_beam.png")),
                            new Vector2f(2 / 16F, 2 / 16F),
                            45F,
                            20F,
                            RenderTypeRegistry.ENTITY_TRANSLUCENT,
                            0
                    ));
        }
    }
}
