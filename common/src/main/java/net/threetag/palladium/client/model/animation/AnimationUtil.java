package net.threetag.palladium.client.model.animation;

import net.minecraft.client.model.geom.ModelPart;

public class AnimationUtil {

    public static void interpolateXTo(ModelPart modelPart, float destination, float progress) {
        modelPart.x += (destination - modelPart.x) * progress;
    }

    public static void interpolateYTo(ModelPart modelPart, float destination, float progress) {
        modelPart.y += (destination - modelPart.y) * progress;
    }

    public static void interpolateZTo(ModelPart modelPart, float destination, float progress) {
        modelPart.z += (destination - modelPart.z) * progress;
    }

    public static void interpolateXRotTo(ModelPart modelPart, float destination, float progress) {
        modelPart.xRot += (destination - modelPart.xRot) * progress;
    }

    public static void interpolateYRotTo(ModelPart modelPart, float destination, float progress) {
        modelPart.yRot += (destination - modelPart.yRot) * progress;
    }

    public static void interpolateZRotTo(ModelPart modelPart, float destination, float progress) {
        modelPart.zRot += (destination - modelPart.zRot) * progress;
    }

    public static float smooth(float value) {
        float sqt = value * value;
        return sqt / (2F * (sqt - value) + 1F);
    }

}
