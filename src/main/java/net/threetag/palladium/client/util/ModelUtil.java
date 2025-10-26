package net.threetag.palladium.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.entity.ArmSetting;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.HideModelPartAbility;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

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

    public static ModelPart getPartFromModel(Model<?> model, String name) {
        if (name.equals("root")) {
            return model.root();
        } else if (name.contains(".")) {
            String[] split = name.split("\\.");
            ModelPart part = model.root().getChild(split[0]);
            for (int i = 1; i < split.length; i++) {
                part = part.getChild(split[i]);
            }
            return part;
        } else {
            return model.root().getChild(name);
        }
    }

    public static Set<String> getHiddenModelPartNames(LivingEntity entity) {
        Set<String> parts = new HashSet<>();

        for (AbilityInstance<HideModelPartAbility> instance : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.HIDE_MODEL_PART.get())) {
            parts.addAll(instance.getAbility().modelParts);
        }

        for (Holder<Customization> holder : EntityCustomizationHandler.get(entity).getSelected()) {
            parts.addAll(holder.value().getHiddenModelParts());
        }

        return parts;
    }

    @SuppressWarnings("rawtypes")
    public static Matrix4f getTransformationMatrix(String part, Vector3f offset, HumanoidModel<?> model, AbstractClientPlayer player, float partialTicks) {
        var poseStack = new PoseStack();
        var modelPart = getPartFromModel(model, part);

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

    public static Matrix4f getTransformationMatrix(String part, Vector3f offset, AbstractClientPlayer player, float partialTicks) {
        if (player instanceof PlayerModelCacheExtension ext) {
            return getTransformationMatrix(part, offset, ext.palladium$getCachedModel(), player, partialTicks);
        } else {
            return new Matrix4f();
        }
    }

    public static Vec3 getInWorldPosition(String part, Vector3f offset, HumanoidModel<?> model, AbstractClientPlayer player, float partialTicks) {
        Vector3f vec = new Vector3f(0, 0, 0);
        vec = getTransformationMatrix(part, offset, model, player, partialTicks).transformPosition(vec);
        return player.getPosition(partialTicks).add(vec.x, vec.y, vec.z);
    }

    public static Vec3 getInWorldPosition(String part, Vector3f offset, AbstractClientPlayer player, float partialTicks) {
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
