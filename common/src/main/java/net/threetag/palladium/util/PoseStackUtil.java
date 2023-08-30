package net.threetag.palladium.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class PoseStackUtil extends PoseStack {

    private final PoseStack poseStack;

    public PoseStackUtil(PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    public PoseStackUtil rotateX(float degrees) {
        this.poseStack.mulPose(Axis.XP.rotationDegrees(degrees));
        return this;
    }

    public PoseStackUtil rotateY(float degrees) {
        this.poseStack.mulPose(Axis.YP.rotationDegrees(degrees));
        return this;
    }

    public PoseStackUtil rotateZ(float degrees) {
        this.poseStack.mulPose(Axis.ZP.rotationDegrees(degrees));
        return this;
    }

    public void translate(double x, double y, double z) {
        this.poseStack.translate(x, y, z);
    }

    public void scale(float x, float y, float z) {
        this.poseStack.scale(x, y, z);
    }

    public void mulPose(Quaternionf quaternion) {
        this.poseStack.mulPose(quaternion);
    }

    public void pushPose() {
        this.poseStack.pushPose();
    }

    public void popPose() {
        this.poseStack.popPose();
    }

    public Pose last() {
        return this.poseStack.last();
    }

    public boolean clear() {
        return this.poseStack.clear();
    }

    public void setIdentity() {
        this.poseStack.setIdentity();
    }

    public void mulPoseMatrix(Matrix4f matrix) {
        this.poseStack.mulPoseMatrix(matrix);
    }


}
