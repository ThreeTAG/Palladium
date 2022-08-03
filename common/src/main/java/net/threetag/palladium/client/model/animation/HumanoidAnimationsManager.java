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
    public static final Map<ModelPart, ModelPartState> CACHE = new HashMap<>();
    public static float PARTIAL_TICK = 0F;

    public static void registerAnimation(Animation animation) {
        ANIMATIONS.add(animation);
        ANIMATIONS.sort(Comparator.comparingInt(Animation::getPriority).reversed());
    }

    public static void pre(AgeableListModel<?> model, Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts, LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        for (ModelPart bodyPart : headParts) {
            CACHE.put(bodyPart, new ModelPartState(bodyPart));
        }

        for (ModelPart bodyPart : bodyParts) {
            CACHE.put(bodyPart, new ModelPartState(bodyPart));
        }

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

    public static void post(Iterable<ModelPart> headParts, Iterable<ModelPart> bodyParts) {
        for (ModelPart bodyPart : headParts) {
            ModelPartState state = CACHE.get(bodyPart);
            if (state != null) {
                state.apply(bodyPart);
            }
        }

        for (ModelPart bodyPart : bodyParts) {
            ModelPartState state = CACHE.get(bodyPart);
            if (state != null) {
                state.apply(bodyPart);
            }
        }

        CACHE.clear();
    }

    public static class ModelPartState {

        public final float x;
        public final float y;
        public final float z;
        public final float xRot;
        public final float yRot;
        public final float zRot;
        public final boolean visible;

        public ModelPartState(ModelPart modelPart) {
            this.x = modelPart.x;
            this.y = modelPart.y;
            this.z = modelPart.z;
            this.xRot = modelPart.xRot;
            this.yRot = modelPart.yRot;
            this.zRot = modelPart.zRot;
            this.visible = modelPart.visible;
        }

        public void apply(ModelPart modelPart) {
            modelPart.x = this.x;
            modelPart.y = this.y;
            modelPart.z = this.z;
            modelPart.xRot = this.xRot;
            modelPart.yRot = this.yRot;
            modelPart.zRot = this.zRot;
            modelPart.visible = this.visible;
        }
    }

}
