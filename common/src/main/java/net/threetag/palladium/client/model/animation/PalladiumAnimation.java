package net.threetag.palladium.client.model.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.util.Easing;

import java.util.*;

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
        private float limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch;

        public Builder() {

        }

        public Builder(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
            this.limbSwing = limbSwing;
            this.limbSwingAmount = limbSwingAmount;
            this.ageInTicks = ageInTicks;
            this.netHeadYaw = netHeadYaw;
            this.headPitch = headPitch;
        }

        public PartAnimationData get(PlayerModelPart modelPart) {
            if (modelPart == null) {
                throw new RuntimeException("Player model part cant be null");
            }

            return this.animationData.computeIfAbsent(modelPart, bodyPart -> new PartAnimationData());
        }

        public Map<PlayerModelPart, PartAnimationData> getAnimationData() {
            return animationData;
        }

        public float getLimbSwing() {
            return limbSwing;
        }

        public float getLimbSwingAmount() {
            return limbSwingAmount;
        }

        public float getAgeInTicks() {
            return ageInTicks;
        }

        public float getNetHeadYaw() {
            return netHeadYaw;
        }

        public float getHeadPitch() {
            return headPitch;
        }
    }

    public enum FirstPersonContext {

        NONE, RIGHT_ARM, LEFT_ARM;

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

        HEAD("head"), CHEST("chest"), RIGHT_ARM("right_arm"), LEFT_ARM("left_arm"), RIGHT_LEG("right_leg"), LEFT_LEG("left_leg"), BODY("body");

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

        private final Map<PartOperationTarget, List<PartOperation>> operations = new LinkedHashMap<>();
        private float multiplier = 1F;

        public PartAnimationData setXRot(float rot) {
            this.operations.computeIfAbsent(PartOperationTarget.X_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.SET, rot));
            return this;
        }

        public PartAnimationData setXRotDegrees(float degrees) {
            return this.setXRot((float) Math.toRadians(degrees));
        }

        public PartAnimationData setYRot(float rot) {
            this.operations.computeIfAbsent(PartOperationTarget.Y_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.SET, rot));
            return this;
        }

        public PartAnimationData setYRotDegrees(float degrees) {
            return this.setYRot((float) Math.toRadians(degrees));
        }

        public PartAnimationData setZRot(float rot) {
            this.operations.computeIfAbsent(PartOperationTarget.Z_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.SET, rot));
            return this;
        }

        public PartAnimationData setZRotDegrees(float degrees) {
            return this.setZRot((float) Math.toRadians(degrees));
        }

        public PartAnimationData rotateX(float rot) {
            this.operations.computeIfAbsent(PartOperationTarget.X_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, rot));
            return this;
        }

        public PartAnimationData rotateXDegrees(float degrees) {
            return this.rotateX((float) Math.toRadians(degrees));
        }

        public PartAnimationData rotateY(float rot) {
            this.operations.computeIfAbsent(PartOperationTarget.Y_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, rot));
            return this;
        }

        public PartAnimationData rotateYDegrees(float degrees) {
            return this.rotateY((float) Math.toRadians(degrees));
        }

        public PartAnimationData rotateZ(float rot) {
            this.operations.computeIfAbsent(PartOperationTarget.Z_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, rot));
            return this;
        }

        public PartAnimationData rotateZDegrees(float degrees) {
            return this.rotateZ((float) Math.toRadians(degrees));
        }

        public PartAnimationData resetXRot() {
            this.operations.computeIfAbsent(PartOperationTarget.X_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.RESET, 0F));
            return this;
        }

        public PartAnimationData resetYRot() {
            this.operations.computeIfAbsent(PartOperationTarget.Y_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.RESET, 0F));
            return this;
        }

        public PartAnimationData resetZRot() {
            this.operations.computeIfAbsent(PartOperationTarget.Z_ROT, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.RESET, 0F));
            return this;
        }

        public PartAnimationData moveX(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.X, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, amount));
            return this;
        }

        @Deprecated
        public PartAnimationData translateX(float amount) {
            return this.setX(amount);
        }

        public PartAnimationData setX(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.X, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.SET, amount));
            return this;
        }

        public PartAnimationData resetX() {
            this.operations.computeIfAbsent(PartOperationTarget.X, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.RESET, 0F));
            return this;
        }

        public PartAnimationData moveY(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.Y, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, amount));
            return this;
        }

        @Deprecated
        public PartAnimationData translateY(float amount) {
            return this.setY(amount);
        }

        public PartAnimationData setY(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.Y, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.SET, amount));
            return this;
        }

        public PartAnimationData setY2(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.Y2, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.SET, amount));
            return this;
        }

        public PartAnimationData resetY() {
            this.operations.computeIfAbsent(PartOperationTarget.Y, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.RESET, 0F));
            return this;
        }

        public PartAnimationData moveZ(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.Z, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, amount));
            return this;
        }

        @Deprecated
        public PartAnimationData translateZ(float amount) {
            return this.setZ(amount);
        }

        public PartAnimationData setZ(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.Z, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.SET, amount));
            return this;
        }

        public PartAnimationData resetZ() {
            this.operations.computeIfAbsent(PartOperationTarget.Z, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.RESET, 0F));
            return this;
        }

        public PartAnimationData scaleX(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.X_SCALE, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, amount));
            return this;
        }

        public PartAnimationData scaleY(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.Y_SCALE, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, amount));
            return this;
        }

        public PartAnimationData scaleZ(float amount) {
            this.operations.computeIfAbsent(PartOperationTarget.Z_SCALE, t -> new LinkedList<>()).add(new PartOperation(PartOperationType.ADD, amount));
            return this;
        }

        public PartAnimationData multiplier(float multiplier) {
            this.multiplier = multiplier;
            return this;
        }

        /**
         * <a href="https://easings.net/#">All available easings</a>
         */
        public PartAnimationData animate(Easing ease, float animationProgress) {
            return this.multiplier(ease.apply(animationProgress));
        }

        public void apply(ModelPart modelPart) {
            for (Map.Entry<PartOperationTarget, List<PartOperation>> entry : this.operations.entrySet()) {
                for (PartOperation operation : entry.getValue()) {
                    operation.apply(modelPart, entry.getKey(), this.multiplier);
                }
            }
        }

        public void apply(PoseStackResult result) {
            for (Map.Entry<PartOperationTarget, List<PartOperation>> entry : this.operations.entrySet()) {
                for (PartOperation operation : entry.getValue()) {
                    operation.apply(result, entry.getKey(), this.multiplier);
                }
            }
        }

    }

    public enum PartOperationTarget {

        X_ROT(0, false), Y_ROT(0, false), Z_ROT(0, false),
        X(0, false), Y(0, false), Z(0, false),
        X2(0, false), Y2(0, false), Z2(0, false),
        X_SCALE(1, true), Y_SCALE(1, true), Z_SCALE(1, true);

        private final float initial;
        private final boolean scale;

        PartOperationTarget(float initial, boolean scale) {
            this.initial = initial;
            this.scale = scale;
        }

        public float get(ModelPart part) {
            return switch (this) {
                case X_ROT -> part.xRot;
                case Y_ROT -> part.yRot;
                case Z_ROT -> part.zRot;
                case X -> part.x;
                case Y -> part.y;
                case Z -> part.z;
                case X_SCALE -> part.xScale;
                case Y_SCALE -> part.yScale;
                case Z_SCALE -> part.zScale;
                default -> 0;
            };
        }

        public float get(PoseStackResult result) {
            return switch (this) {
                case X_ROT -> result.xRot;
                case Y_ROT -> result.yRot;
                case Z_ROT -> result.zRot;
                case X -> result.x;
                case Y -> result.y;
                case Z -> result.z;
                case X2 -> result.x2;
                case Y2 -> result.y2;
                case Z2 -> result.z2;
                case X_SCALE -> result.xScale;
                case Y_SCALE -> result.yScale;
                case Z_SCALE -> result.zScale;
            };
        }

        public float get(PartPose pose) {
            return switch (this) {
                case X_ROT -> pose.xRot;
                case Y_ROT -> pose.yRot;
                case Z_ROT -> pose.zRot;
                case X -> pose.x;
                case Y -> pose.y;
                case Z -> pose.z;
                case X2, Y2, Z2 -> 0;
                default -> 1F;
            };
        }

        public void set(ModelPart part, float value) {
            switch (this) {
                case X_ROT -> part.xRot = value;
                case Y_ROT -> part.yRot = value;
                case Z_ROT -> part.zRot = value;
                case X -> part.x = value;
                case Y -> part.y = value;
                case Z -> part.z = value;
                case X_SCALE -> part.xScale = value;
                case Y_SCALE -> part.yScale = value;
                case Z_SCALE -> part.zScale = value;
            }
        }

        public void set(PoseStackResult result, float value) {
            switch (this) {
                case X_ROT -> result.xRot = value;
                case Y_ROT -> result.yRot = value;
                case Z_ROT -> result.zRot = value;
                case X -> result.x = value;
                case Y -> result.y = value;
                case Z -> result.z = value;
                case X2 -> result.x2 = value;
                case Y2 -> result.y2 = value;
                case Z2 -> result.z2 = value;
                case X_SCALE -> result.xScale = value;
                case Y_SCALE -> result.yScale = value;
                case Z_SCALE -> result.zScale = value;
            }
        }
    }

    public enum PartOperationType {

        ADD, ADD_INITIAL, SET, RESET;

    }

    public static class PartOperation {

        private final PartOperationType type;
        private final float value;

        private PartOperation(PartOperationType type, float value) {
            this.type = type;
            this.value = value;
        }

        public void apply(ModelPart part, PartOperationTarget target, float multiplier) {
            if (this.type == PartOperationType.SET) {
                var current = target.get(part);
                target.set(part, current + (this.value - current) * multiplier);
            } else if (this.type == PartOperationType.ADD) {
                if (target.scale) {
                    target.set(part, target.get(part) * (1 + (this.value - 1) * multiplier));
                } else {
                    target.set(part, target.get(part) + this.value * multiplier);
                }
            } else {
                var current = target.get(part);
                target.set(part, current + (target.get(part.getInitialPose()) - current) * multiplier);
            }
        }

        public void apply(PoseStackResult result, PartOperationTarget target, float multiplier) {
            if (this.type == PartOperationType.SET) {
                var current = target.get(result);
                target.set(result, current + (this.value - current) * multiplier);
            } else if (this.type == PartOperationType.ADD) {
                if (target.scale) {
                    target.set(result, target.get(result) * (1 + (this.value - 1) * multiplier));
                } else {
                    target.set(result, target.get(result) + this.value * multiplier);
                }
            } else {
                var current = target.get(result);
                target.set(result, current + (target.initial - current) * multiplier);
            }
        }
    }

    public static class PoseStackResult {

        private float x = 0, y = 0, z = 0;
        private float x2 = 0, y2 = 0, z2 = 0;
        private float xRot = 0, yRot = 0, zRot = 0;
        private float xScale = 1F, yScale = 1F, zScale = 1F;

        public void apply(PoseStack poseStack) {
            poseStack.translate(this.x / 16.0F, this.y / 16.0F, this.z / 16.0F);

            if (this.zRot != 0.0F) {
                poseStack.mulPose(Axis.ZP.rotation(this.zRot));
            }

            if (this.yRot != 0.0F) {
                poseStack.mulPose(Axis.YP.rotation(this.yRot));
            }

            if (this.xRot != 0.0F) {
                poseStack.mulPose(Axis.XP.rotation(this.xRot));
            }

            poseStack.translate(this.x2 / 16.0F, this.y2 / 16.0F, this.z2 / 16.0F);

            if (this.xScale != 1.0F || this.yScale != 1.0F || this.zScale != 1.0F) {
                poseStack.scale(this.xScale, this.yScale, this.zScale);
            }
        }

    }

}
