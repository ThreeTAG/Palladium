package net.threetag.palladium.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

public record PoseStackTransformation(Vec3 scale, Vec3 translation, Vec3 rotation) {

    private static final Vec3 DEFAULT_SCALE = new Vec3(1, 1, 1);

    public static final Codec<Vec3> SCALE_CODEC = Codec.either(Codec.DOUBLE, Vec3.CODEC).xmap(
            either -> either.map(
                    d -> new Vec3(d, d, d),
                    v -> v
            ),
            vec -> vec.x == vec.y && vec.x == vec.z ? Either.left(vec.x) : Either.right(vec)
    );

    public static final Codec<PoseStackTransformation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SCALE_CODEC.optionalFieldOf("scale", DEFAULT_SCALE).forGetter(PoseStackTransformation::scale),
            Vec3.CODEC.optionalFieldOf("translation", Vec3.ZERO).forGetter(PoseStackTransformation::translation),
            Vec3.CODEC.optionalFieldOf("rotation", Vec3.ZERO).forGetter(PoseStackTransformation::rotation)
    ).apply(instance, PoseStackTransformation::new));

    public PoseStackTransformation invertYRot() {
        return new PoseStackTransformation(
                this.scale,
                new Vec3(this.translation.x, this.translation.y, this.translation.z),
                new Vec3(this.rotation.x, -this.rotation.y, this.rotation.z)
        );
    }

    @Environment(EnvType.CLIENT)
    public void apply(PoseStack poseStack) {
        var translation = this.translation();
        poseStack.translate(translation.x, translation.y, translation.z);

        var scale = this.scale();
        poseStack.scale((float) scale.x, (float) scale.y, (float) scale.z);

        var rotation = this.rotation();
        poseStack.mulPose(new Quaternionf()
                .rotationXYZ((float) (rotation.x * (Math.PI / 180.0)), (float) (rotation.y * (Math.PI / 180.0)), (float) (rotation.z * (Math.PI / 180.0))));
    }

    public static PoseStackTransformation create(float scale, float translationX, float translationY, float translationZ, float rotationX, float rotationY, float rotationZ) {
        return new PoseStackTransformation(new Vec3(scale, scale, scale), new Vec3(translationX, translationY, translationZ), new Vec3(rotationX, rotationY, rotationZ));
    }

    public static PoseStackTransformation lerp(
            float progress, PoseStackTransformation start, PoseStackTransformation end
    ) {
        if (progress >= 1F) {
            return end;
        }

        Vec3 startScale = start.scale();
        Vec3 endScale = end.scale();
        Vec3 startRotation = start.rotation();
        Vec3 endRotation = end.rotation();
        Vec3 startTranslation = start.translation();
        Vec3 endTranslation = end.translation();

        return new PoseStackTransformation(
                new Vec3(
                        Mth.lerp(progress, startScale.x(), endScale.x()),
                        Mth.lerp(progress, startScale.y(), endScale.y()),
                        Mth.lerp(progress, startScale.z(), endScale.z())
                ),
                new Vec3(
                        Mth.lerp(progress, startTranslation.x(), endTranslation.x()),
                        Mth.lerp(progress, startTranslation.y(), endTranslation.y()),
                        Mth.lerp(progress, startTranslation.z(), endTranslation.z())
                ),
                new Vec3(
                        Mth.lerp(progress, startRotation.x(), endRotation.x()),
                        Mth.lerp(progress, startRotation.y(), endRotation.y()),
                        Mth.lerp(progress, startRotation.z(), endRotation.z())
                )
        );
    }

}
