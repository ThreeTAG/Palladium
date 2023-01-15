package net.threetag.palladium.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

public class PoseStackUtil {

    private final PoseStack poseStack;

    public PoseStackUtil(PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    public PoseStackUtil rotateX(float degrees) {
        this.poseStack.mulPose(Vector3f.XP.rotationDegrees(degrees));
        return this;
    }

    public PoseStackUtil rotateY(float degrees) {
        this.poseStack.mulPose(Vector3f.YP.rotationDegrees(degrees));
        return this;
    }

    public PoseStackUtil rotateZ(float degrees) {
        this.poseStack.mulPose(Vector3f.ZP.rotationDegrees(degrees));
        return this;
    }

}
