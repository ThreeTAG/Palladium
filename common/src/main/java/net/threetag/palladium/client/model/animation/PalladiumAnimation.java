package net.threetag.palladium.client.model.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.core.util.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;

import java.util.HashMap;
import java.util.Map;

public class PalladiumAnimation {

    private final int priority;

    public PalladiumAnimation(int priority) {
        this.priority = priority;
    }

    public void animate(Builder builder, AbstractClientPlayer player, HumanoidModel<?> model, FirstPersonContext firstPersonContext, float partialTicks) {
    }

    public int getPriority() {
        return priority;
    }

    public static class Builder {

        private final Map<PlayerModelPart, PartAnimationData> animationData = new HashMap<>();

        public PartAnimationData get(PlayerModelPart modelPart) {
            if (modelPart == null) {
                throw new RuntimeException("Player model part cant be null");
            }

            return this.animationData.computeIfAbsent(modelPart, bodyPart -> new PartAnimationData());
        }

        public Map<PlayerModelPart, PartAnimationData> getAnimationData() {
            return animationData;
        }
    }

    public enum FirstPersonContext {

        NONE,
        RIGHT_ARM,
        LEFT_ARM;

        public boolean firstPerson() {
            return this != NONE;
        }

        public boolean rightArm() {
            return this == RIGHT_ARM;
        }

        public boolean leftArm() {
            return this == LEFT_ARM;
        }

        public boolean mainArm() {
            var player = Minecraft.getInstance().player;
            return player != null && player.getMainArm() == HumanoidArm.RIGHT && this == RIGHT_ARM;
        }

        public boolean offArm() {
            var player = Minecraft.getInstance().player;
            return player != null && player.getMainArm() == HumanoidArm.RIGHT && this == LEFT_ARM;
        }

    }

    public enum PlayerModelPart {

        HEAD("head"),
        CHEST("chest"),
        RIGHT_ARM("right_arm"),
        LEFT_ARM("left_arm"),
        RIGHT_LEG("right_leg"),
        LEFT_LEG("left_leg"),
        BODY("body");

        PlayerModelPart(String name) {
            this.name = name;
        }

        private final String name;

        public void applyToModelPart(HumanoidModel<?> model, PartAnimationData data) {
            if (this == HEAD) {
                data.apply(model.head);
                model.hat.copyFrom(model.head);
            } else if (this == CHEST) {
                data.apply(model.body);
            } else if (this == RIGHT_ARM) {
                data.apply(model.rightArm);
            } else if (this == LEFT_ARM) {
                data.apply(model.leftArm);
            } else if (this == RIGHT_LEG) {
                data.apply(model.rightLeg);
            } else if (this == LEFT_LEG) {
                data.apply(model.leftLeg);
            }

            if (model instanceof PlayerModel<?> playerModel) {
                playerModel.jacket.copyFrom(model.body);
                playerModel.rightSleeve.copyFrom(model.rightArm);
                playerModel.leftSleeve.copyFrom(model.leftArm);
                playerModel.rightPants.copyFrom(model.rightLeg);
                playerModel.leftPants.copyFrom(model.leftLeg);
            }
        }

        public static PlayerModelPart fromName(String name) {
            for (PlayerModelPart part : values()) {
                if (part.name.equalsIgnoreCase(name)) {
                    return part;
                }
            }
            return null;
        }

    }

    public static class PartAnimationData {

        private Float xRot = null, yRot = null, zRot = null;
        private Float translationX = null, translationY = null, translationZ = null;
        private float scaleX = 1F, scaleY = 1F, scaleZ = 1F;
        private float multiplier = 1F;

        public PartAnimationData rotateX(float rot) {
            if (this.xRot == null) {
                this.xRot = 0F;
            }
            this.xRot += rot;
            return this;
        }

        public PartAnimationData rotateXDegrees(float degrees) {
            return this.rotateX((float) Math.toRadians(degrees));
        }

        public PartAnimationData rotateY(float rot) {
            if (this.yRot == null) {
                this.yRot = 0F;
            }
            this.yRot += rot;
            return this;
        }

        public PartAnimationData rotateYDegrees(float degrees) {
            return this.rotateY((float) Math.toRadians(degrees));
        }

        public PartAnimationData rotateZ(float rot) {
            if (this.zRot == null) {
                this.zRot = 0F;
            }
            this.zRot += rot;
            return this;
        }

        public PartAnimationData rotateZDegrees(float degrees) {
            return this.rotateZ((float) Math.toRadians(degrees));
        }

        public PartAnimationData translateX(float amount) {
            if (this.translationX == null) {
                this.translationX = 0F;
            }
            this.translationX += amount;
            return this;
        }

        public PartAnimationData translateY(float amount) {
            if (this.translationY == null) {
                this.translationY = 0F;
            }
            this.translationY += amount;
            return this;
        }

        public PartAnimationData translateZ(float amount) {
            if (this.translationZ == null) {
                this.translationZ = 0F;
            }
            this.translationZ += amount;
            return this;
        }

        public PartAnimationData scale(float multiplier) {
            this.scaleX *= multiplier;
            this.scaleY *= multiplier;
            this.scaleY *= multiplier;
            return this;
        }

        public PartAnimationData scaleX(float multiplier) {
            this.scaleX *= multiplier;
            return this;
        }

        public PartAnimationData scaleY(float multiplier) {
            this.scaleY *= multiplier;
            return this;
        }

        public PartAnimationData scaleZ(float multiplier) {
            this.scaleZ *= multiplier;
            return this;
        }

        public PartAnimationData multiplier(float multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        /**
         * <a href="https://easings.net/#">All available easings</a>
         */
        public PartAnimationData animate(Ease ease, float animationProgress) {
            return this.multiplier(Easing.easingFromEnum(ease, animationProgress));
        }

        public void apply(ModelPart modelPart) {
            if (this.xRot != null)
                modelPart.xRot += (this.xRot - modelPart.xRot) * this.multiplier;
            if (this.yRot != null)
                modelPart.yRot += (this.yRot - modelPart.yRot) * this.multiplier;
            if (this.zRot != null)
                modelPart.zRot += (this.zRot - modelPart.zRot) * this.multiplier;

            if (this.translationX != null)
                modelPart.x += (this.translationX - modelPart.x) * this.multiplier;
            if (this.translationY != null)
                modelPart.y += (this.translationY - modelPart.y) * this.multiplier;
            if (this.translationZ != null)
                modelPart.z += (this.translationZ - modelPart.z) * this.multiplier;

            modelPart.xScale += (this.scaleX - modelPart.xScale) * this.multiplier;
            modelPart.yScale += (this.scaleY - modelPart.yScale) * this.multiplier;
            modelPart.zScale += (this.scaleZ - modelPart.zScale) * this.multiplier;
        }

        public void apply(PoseStack poseStack) {
            if (this.translationX != null)
                poseStack.translate(this.translationX * this.multiplier, 0, 0);
            if (this.translationY != null)
                poseStack.translate(0, this.translationY * this.multiplier, 0);
            if (this.translationZ != null)
                poseStack.translate(0, 0, this.translationZ * this.multiplier);

            if (this.xRot != null)
                poseStack.mulPose(Vector3f.XP.rotation(this.xRot * this.multiplier));
            if (this.yRot != null)
                poseStack.mulPose(Vector3f.YP.rotation(this.yRot * this.multiplier));
            if (this.zRot != null)
                poseStack.mulPose(Vector3f.ZP.rotation(this.zRot * this.multiplier));

            poseStack.scale(this.scaleX, this.scaleY, this.scaleZ);
        }

    }

}
