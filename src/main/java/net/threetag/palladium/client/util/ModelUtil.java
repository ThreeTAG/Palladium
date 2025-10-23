package net.threetag.palladium.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerCapeModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.ArmSetting;
import net.threetag.palladium.entity.BodyPart;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ModelUtil {

    public static void copyProperties(ModelPart from, ModelPart to) {
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;
        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;
        to.visible = from.visible;
    }

    @Nullable
    public static ModelPart getModelPartByBodyPart(HumanoidModel<?> model, BodyPart bodyPart) {
        PlayerModel playerModel = model instanceof PlayerModel pl ? pl : null;

        return switch (bodyPart) {
            case HEAD -> model.head;
            case HEAD_OVERLAY -> model.hat;
            case CHEST -> model.body;
            case CHEST_OVERLAY -> playerModel != null ? playerModel.jacket : null;
            case RIGHT_ARM -> model.rightArm;
            case RIGHT_ARM_OVERLAY -> playerModel != null ? playerModel.rightSleeve : null;
            case LEFT_ARM -> model.leftArm;
            case LEFT_ARM_OVERLAY -> playerModel != null ? playerModel.leftSleeve : null;
            case RIGHT_LEG -> model.rightLeg;
            case RIGHT_LEG_OVERLAY -> playerModel != null ? playerModel.rightPants : null;
            case LEFT_LEG -> model.leftLeg;
            case LEFT_LEG_OVERLAY -> playerModel != null ? playerModel.leftPants : null;
            case CAPE -> model instanceof PlayerCapeModel capeModel ? capeModel.cape : null;
        };
    }

    public static void setVisibilityByBodyPart(HumanoidModel<?> model, BodyPart bodyPart, boolean visible) {
        ModelPart part = getModelPartByBodyPart(model, bodyPart);

        if (part != null) {
            part.visible = visible;
        }
    }

    @SuppressWarnings("rawtypes")
    public static Matrix4f getTransformationMatrix(BodyPart part, Vector3f offset, HumanoidModel<?> model, AbstractClientPlayer player, float partialTicks) {
        var poseStack = new PoseStack();
        var modelPart = getModelPartByBodyPart(model, part);

        if (modelPart == null) {
            return poseStack.last().pose();
        }

        EntityRenderer entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);

        if (entityRenderer instanceof AvatarRenderer renderer && renderer.createRenderState(player, partialTicks) instanceof AvatarRenderState state) {
            if (state.hasPose(Pose.SLEEPING)) {
                Direction direction = state.bedOrientation;
                if (direction != null) {
                    float f = state.eyeHeight - 0.1F;
                    poseStack.translate((float) (-direction.getStepX()) * f, 0.0F, (float) (-direction.getStepZ()) * f);
                }
            }

            float g = state.scale;
            poseStack.scale(g, g, g);
            renderer.setupRotations(state, poseStack, state.bodyRot, g);
            poseStack.scale(-1.0F, -1.0F, 1.0F);
            renderer.scale(state, poseStack);
            poseStack.translate(0.0F, -1.501F, 0.0F);
            modelPart.translateAndRotate(poseStack);
            poseStack.translate(offset.x, offset.y, offset.z);
        }

        return poseStack.last().pose();
    }

    public static Matrix4f getTransformationMatrix(BodyPart part, Vector3f offset, AbstractClientPlayer player, float partialTicks) {
        if (player instanceof PlayerModelCacheExtension ext) {
            return getTransformationMatrix(part, offset, ext.palladium$getCachedModel(), player, partialTicks);
        } else {
            return new Matrix4f();
        }
    }

    public static Vec3 getInWorldPosition(BodyPart part, Vector3f offset, HumanoidModel<?> model, AbstractClientPlayer player, float partialTicks) {
        Vector3f vec = new Vector3f(0, 0, 0);
        vec = getTransformationMatrix(part, offset, model, player, partialTicks).transformPosition(vec);
        return player.getPosition(partialTicks).add(vec.x, vec.y, vec.z);
    }

    public static Vec3 getInWorldPosition(BodyPart part, Vector3f offset, AbstractClientPlayer player, float partialTicks) {
        if (player instanceof PlayerModelCacheExtension ext) {
            return getInWorldPosition(part, offset, ext.palladium$getCachedModel(), player, partialTicks);
        } else {
            return player.getPosition(partialTicks);
        }
    }

    public ModelPart[] getModelPartFromArmSetting(LivingEntity entity, HumanoidModel<?> model, ArmSetting setting) {
        if (setting == ArmSetting.NONE) {
            return new ModelPart[0];
        } else if (setting == ArmSetting.MAIN_ARM) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                return new ModelPart[]{model.rightArm};
            } else {
                return new ModelPart[]{model.leftArm};
            }
        } else if (setting == ArmSetting.OFF_ARM) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                return new ModelPart[]{model.leftArm};
            } else {
                return new ModelPart[]{model.rightArm};
            }
        } else if (setting == ArmSetting.RIGHT_ARM) {
            return new ModelPart[]{model.rightArm};
        } else if (setting == ArmSetting.LEFT_ARM) {
            return new ModelPart[]{model.leftArm};
        } else {
            return new ModelPart[]{model.rightArm, model.leftArm};
        }
    }

}
