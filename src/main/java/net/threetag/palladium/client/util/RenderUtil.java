package net.threetag.palladium.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;

public class RenderUtil {

    public static final ResourceLocation WIDGETS_LOCATION = Palladium.id("textures/gui/widgets.png");
    public static final int FULL_WHITE = ARGB.white(1F);
    public static final int FULL_BLACK = ARGB.color(255, 0, 0, 0);
    public static final int DEFAULT_GRAY = ARGB.opaque(0x404040);

    public static void faceVec(PoseStack poseStack, Vec3 src, Vec3 dst) {
        double x = dst.x - src.x;
        double y = dst.y - src.y;
        double z = dst.z - src.z;
        double diff = Mth.sqrt((float) (x * x + z * z));
        float yaw = (float) (Math.atan2(z, x) * 180 / Math.PI) - 90;
        float pitch = (float) -(Math.atan2(y, diff) * 180 / Math.PI);

        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
    }

    public static void submitFilledBox(PoseStack stack, RenderType renderType, SubmitNodeCollector submitNodeCollector, AABB box, float red, float green, float blue, float alpha, int combinedLightIn) {
        submitNodeCollector.submitCustomGeometry(stack, renderType, (pose, vertexConsumer) -> {
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.maxY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.maxY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.maxY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.maxY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);

            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);

            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.maxY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.maxY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);

            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.maxY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.maxY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);

            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.maxY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.maxY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);

            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.maxY, (float) box.maxZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.maxY, (float) box.minZ).setColor(red, green, blue, alpha).setLight(combinedLightIn);
        });

    }

    public static void submitTexturedBox(PoseStack stack, RenderType renderType, SubmitNodeCollector submitNodeCollector, AABB box, int color, int combinedLightIn, int packedOverlay, float heightMultiplier) {

        submitNodeCollector.submitCustomGeometry(stack, renderType, (pose, vertexConsumer) -> {
            var yDiff = (float) (box.maxY - box.minY);
            float maxY = (float) (box.minY + yDiff * heightMultiplier);

            vertexConsumer.addVertex(pose, (float) box.minX, maxY, (float) box.minZ).setUv(0, 0).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.minX, maxY, (float) box.maxZ).setUv(0, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, maxY, (float) box.maxZ).setUv(1, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, maxY, (float) box.minZ).setUv(1, 0).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);

            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).setUv(0, 0).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).setUv(1, 0).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).setUv(1, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).setUv(0, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);

            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).setUv(1, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.minX, maxY, (float) box.minZ).setUv(1, 1 - heightMultiplier).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, maxY, (float) box.minZ).setUv(0, 1 - heightMultiplier).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).setUv(0, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);

            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).setUv(0, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).setUv(1, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, maxY, (float) box.maxZ).setUv(1, 1 - heightMultiplier).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.minX, maxY, (float) box.maxZ).setUv(0, 1 - heightMultiplier).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);

            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.minZ).setUv(1, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, maxY, (float) box.minZ).setUv(1, 1 - heightMultiplier).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, maxY, (float) box.maxZ).setUv(0, 1 - heightMultiplier).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.maxX, (float) box.minY, (float) box.maxZ).setUv(0, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);

            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.minZ).setUv(0, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.minX, (float) box.minY, (float) box.maxZ).setUv(1, 1).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.minX, maxY, (float) box.maxZ).setUv(1, 1 - heightMultiplier).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
            vertexConsumer.addVertex(pose, (float) box.minX, maxY, (float) box.minZ).setUv(0, 1 - heightMultiplier).setColor(color).setOverlay(packedOverlay).setLight(combinedLightIn).setNormal(pose, 0.0F, 1.0F, 0.0F);
        });
    }
}
