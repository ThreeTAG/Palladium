package net.threetag.palladium.customization;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public record CustomizationPreview(float scale, Vec3 translation, Vec3 rotation) {

    public static final Codec<CustomizationPreview> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("scale", 1F).forGetter(CustomizationPreview::scale),
            Vec3.CODEC.optionalFieldOf("translation", Vec3.ZERO).forGetter(CustomizationPreview::translation),
            Vec3.CODEC.optionalFieldOf("rotation", Vec3.ZERO).forGetter(CustomizationPreview::rotation)
    ).apply(instance, CustomizationPreview::new));

    public static CustomizationPreview create(float scale, float translationX, float translationY, float translationZ, float rotationX, float rotationY, float rotationZ) {
        return new CustomizationPreview(scale, new Vec3(translationX, translationY, translationZ), new Vec3(rotationX, rotationY, rotationZ));
    }

    public CustomizationPreview invertYRot() {
        return new CustomizationPreview(
                this.scale,
                this.translation,
                this.rotation.multiply(1, -1, 1)
        );
    }

    public static CustomizationPreview lerp(
            float progress, CustomizationPreview start, CustomizationPreview end
    ) {
        if (progress >= 1F) {
            return end;
        }

        float startScale = start.scale();
        float endScale = end.scale();
        Vec3 startRotation = start.rotation();
        Vec3 endRotation = end.rotation();
        Vec3 startTranslation = start.translation();
        Vec3 endTranslation = end.translation();

        return new CustomizationPreview(
                Mth.lerp(progress, startScale, endScale),
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
