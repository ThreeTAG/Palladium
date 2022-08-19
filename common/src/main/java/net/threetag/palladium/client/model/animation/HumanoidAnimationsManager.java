package net.threetag.palladium.client.model.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;

@Environment(EnvType.CLIENT)
public class HumanoidAnimationsManager {

    private static final List<Animation> ANIMATIONS = new LinkedList<>();
    public static float PARTIAL_TICK = 0F;

    public static void registerAnimation(Animation animation) {
        ANIMATIONS.add(animation);
        ANIMATIONS.sort(Comparator.comparingInt(Animation::getPriority).reversed());
    }

    public static void resetModelParts(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts) {
        for (ModelPart bodyPart : headParts) {
            bodyPart.resetPose();
        }

        for (ModelPart bodyPart : bodyParts) {
            bodyPart.resetPose();
        }
    }

    public static void pre(AgeableListModel<?> model, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (model instanceof HumanoidModel<?> humanoidModel) {
            for (Animation animation : ANIMATIONS) {
                if (animation.active(entity)) {
                    animation.setupAnimation(humanoidModel, entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, PARTIAL_TICK);
                    return;
                }
            }
        }
    }

    public static void setupRotations(PlayerRenderer playerRenderer, AbstractClientPlayer player, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        for (Animation animation : ANIMATIONS) {
            if (animation.active(player)) {
                animation.setupRotations(playerRenderer, player, poseStack, ageInTicks, rotationYaw, partialTicks);
                return;
            }
        }
    }

}
