package net.threetag.palladium.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;

import java.awt.*;

public class RenderUtil {

    public static final Identifier WIDGETS_LOCATION = Palladium.id("textures/gui/widgets.png");
    public static final int FULL_WHITE = ARGB.white(1F);
    public static final int FULL_BLACK = ARGB.color(255, 0, 0, 0);
    public static final int DEFAULT_GRAY = ARGB.opaque(0x404040);
    public static final Color DEFAULT_GRAY_COLOR = new Color(ARGB.opaque(0x404040));

    public static void setItemInHumanoidRenderStateSlot(HumanoidRenderState state, EquipmentSlot slot, ItemStack stack) {
        ItemModelResolver itemModelResolver = Minecraft.getInstance().getItemModelResolver();

        if (slot == EquipmentSlot.MAINHAND) {
            var right = state.mainArm == HumanoidArm.RIGHT;

            if (right) {
                state.rightHandItemStack = stack;
                itemModelResolver.updateForTopItem(state.rightHandItemState, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, null, null, 0);
            } else {
                state.leftHandItemStack = stack;
                itemModelResolver.updateForTopItem(state.leftHandItemState, stack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, null, null, 0);
            }
        } else if (slot == EquipmentSlot.OFFHAND) {
            var right = state.mainArm != HumanoidArm.RIGHT;

            if (right) {
                state.rightHandItemStack = stack;
                itemModelResolver.updateForTopItem(state.rightHandItemState, stack, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, null, null, 0);
            } else {
                state.leftHandItemStack = stack;
                itemModelResolver.updateForTopItem(state.leftHandItemState, stack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, null, null, 0);
            }
        } else if (slot == EquipmentSlot.HEAD) {
            state.headEquipment = stack;
            if (!HumanoidArmorLayer.shouldRender(stack, EquipmentSlot.HEAD)) {
                itemModelResolver.updateForTopItem(state.headItem, stack, ItemDisplayContext.HEAD, null, null, 0);
            }
        } else if (slot == EquipmentSlot.CHEST) {
            state.chestEquipment = stack;
        } else if (slot == EquipmentSlot.LEGS) {
            state.legsEquipment = stack;
        } else if (slot == EquipmentSlot.FEET) {
            state.feetEquipment = stack;
        }
    }

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
