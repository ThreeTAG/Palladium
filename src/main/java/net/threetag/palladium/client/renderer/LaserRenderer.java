package net.threetag.palladium.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.ColorLerper;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.util.PalladiumCodecs;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import java.awt.*;
import java.util.Optional;

public record LaserRenderer(net.threetag.palladium.client.renderer.LaserRenderer.LaserPart glow,
                            net.threetag.palladium.client.renderer.LaserRenderer.LaserPart core,
                            int bloom, Vector2f size, float rotation,
                            float rotationSpeed) {

    public static final Codec<Vector2f> SIZE_CODEC = Codec.either(PalladiumCodecs.VOXEL_VECTOR_2F, PalladiumCodecs.NON_NEGATIVE_VOXEL_FLOAT).xmap(
            either -> either.map(vector2f -> vector2f, aFloat -> new Vector2f(aFloat, aFloat)),
            vector2f -> vector2f.x == vector2f.y ? Either.right(vector2f.x) : Either.left(vector2f));

    public static Codec<LaserRenderer> codec(int defaultBloom) {
        return RecordCodecBuilder.create(instance -> instance.group(
                LaserPart.CODEC.optionalFieldOf("glow", LaserPart.DEFAULT).forGetter(LaserRenderer::glow),
                LaserPart.CODEC.optionalFieldOf("core", LaserPart.DEFAULT).forGetter(LaserRenderer::core),
                ExtraCodecs.intRange(0, 10).optionalFieldOf("bloom", defaultBloom).forGetter(LaserRenderer::bloom),
                SIZE_CODEC.optionalFieldOf("size", new Vector2f(1 / 16F, 1 / 16F)).forGetter(LaserRenderer::size),
                ExtraCodecs.floatRange(0F, 360F).optionalFieldOf("rotation", 0F).forGetter(LaserRenderer::rotation),
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("rotation_speed", 0F).forGetter(LaserRenderer::rotationSpeed)
        ).apply(instance, LaserRenderer::new));
    }

    public void face(PoseStack poseStack, Vec3 origin, Vec3 target) {
        RenderUtil.faceVec(poseStack, origin, target);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
    }

    public void faceAndRender(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 origin, Vec3 target, int ticks, float partialTick) {
        this.faceAndRender(poseStack, submitNodeCollector, origin, target, ticks, partialTick, 1F, 1F, Vec2.ONE);
    }

    public void faceAndRender(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 origin, Vec3 target, int ticks, float partialTick, float lengthMultiplier, float opacityMultiplier) {
        this.faceAndRender(poseStack, submitNodeCollector, origin, target, ticks, partialTick, lengthMultiplier, opacityMultiplier, Vec2.ONE);
    }

    public void faceAndRender(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 origin, Vec3 target, int ticks, float partialTick, float lengthMultiplier, float opacityMultiplier, float sizeMultiplier) {
        this.faceAndRender(poseStack, submitNodeCollector, origin, target, ticks, partialTick, lengthMultiplier, opacityMultiplier, new Vec2(sizeMultiplier, sizeMultiplier));
    }

    public void faceAndRender(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 origin, Vec3 target, int ticks, float partialTick, float lengthMultiplier, float opacityMultiplier, Vec2 sizeMultiplier) {
        poseStack.pushPose();
        this.face(poseStack, origin, target);
        this.render(poseStack, submitNodeCollector, ticks, origin.distanceTo(target) * lengthMultiplier, partialTick, opacityMultiplier, sizeMultiplier);
        poseStack.popPose();
    }

    public void render(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int ticks, double length, float partialTick, float opacityMultiplier, Vec2 sizeMultiplier) {
        var rot = this.rotation;

        if (this.rotationSpeed > 0F) {
            rot += (ticks + partialTick) * rotationSpeed;
        }

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(rot % 360F));

        var size = new Vector2f(this.size).mul(sizeMultiplier.x, sizeMultiplier.y).mul(this.core.getPulseScale(ticks + partialTick));
        AABB box = new AABB(-size.x / 2F, 0, -size.y / 2F, size.x / 2F, length, size.y / 2F);

        var coreColor = computeColor(this.core, ticks, partialTick);
        var r = coreColor.getRed() / 255F;
        var g = coreColor.getGreen() / 255F;
        var b = coreColor.getBlue() / 255F;

        if (this.core.opacity > 0F) {
            RenderUtil.submitFilledBox(poseStack, PalladiumRenderTypes.LASER, submitNodeCollector, box, r, g, b, this.core.opacity * opacityMultiplier, LightTexture.FULL_SKY);
        }

        var glowColor = computeColor(this.glow, ticks, partialTick);
        r = glowColor.getRed() / 255F;
        g = glowColor.getGreen() / 255F;
        b = glowColor.getBlue() / 255F;

        if (this.glow.opacity > 0F) {
            float pulse = this.glow.getPulseScale(ticks + partialTick);
            for (int i = 0; i < this.bloom + 1; i++) {
                RenderUtil.submitFilledBox(poseStack, PalladiumRenderTypes.LASER, submitNodeCollector, box.inflate(i * 0.5F * 0.0625F * pulse), r, g, b, (1F / i / 2) * this.glow.opacity * opacityMultiplier, LightTexture.FULL_SKY);
            }
        }

        poseStack.popPose();
    }

    private static Color computeColor(LaserPart part, int ticks, float partialTick) {
        if (part.rainbow > 0F) {
            int rate = Math.max((int) (25 * (1F - part.rainbow)), 1);
            int l = ticks / rate;
            int m = DyeColor.values().length;
            int n = l % m;
            int o = (l + 1) % m;
            float h = ((float) (ticks % rate) + partialTick) / rate;
            int p = ColorLerper.Type.SHEEP.getColor(DyeColor.byId(n));
            int q = ColorLerper.Type.SHEEP.getColor(DyeColor.byId(o));
            return new Color(ARGB.srgbLerp(h, p, q));
        } else {
            return part.color;
        }
    }

    public record LaserPart(Color color, float opacity, float rainbow, @Nullable Pulse pulse) {

        public static final LaserPart DEFAULT = new LaserPart(Color.WHITE, 1F, 0F, null);

        private static final Codec<LaserPart> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PalladiumCodecs.COLOR_CODEC.optionalFieldOf("color", Color.WHITE).forGetter(LaserPart::color),
                ExtraCodecs.floatRange(0F, 1F).optionalFieldOf("opacity", 1F).forGetter(LaserPart::opacity),
                PalladiumCodecs.FLOAT_OR_BOOLEAN_CODEC.optionalFieldOf("rainbow", 0F).forGetter(LaserPart::rainbow),
                Pulse.CODEC.optionalFieldOf("pulse").forGetter(p -> Optional.ofNullable(p.pulse()))
        ).apply(instance, (color, opacity, rainbow, pulse) -> new LaserPart(color, opacity, rainbow, pulse.orElse(null))));

        public static final Codec<LaserPart> CODEC = Codec.either(PalladiumCodecs.COLOR_CODEC, DIRECT_CODEC).xmap(colorLaserPartEither ->
                        colorLaserPartEither.map(color1 -> new LaserPart(color1, 1F, 0F, null), laserPart -> laserPart),
                laserPart -> laserPart.opacity == 1F && laserPart.rainbow == 0F && laserPart.pulse == null ?
                        Either.left(laserPart.color()) :
                        Either.right(laserPart));

        public float getPulseScale(float time) {
            return this.pulse != null ? 1F + (this.pulse.scale() * Mth.sin(time * this.pulse.frequency())) : 1F;
        }

    }

    public record Pulse(float scale, float frequency) {

        public static final Codec<Pulse> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("scale", 1F).forGetter(Pulse::scale),
                ExtraCodecs.floatRange(0F, 10F).optionalFieldOf("frequency", 1F).forGetter(Pulse::frequency)
        ).apply(instance, Pulse::new));

    }
}
