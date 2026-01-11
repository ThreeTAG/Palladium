package net.threetag.palladium.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.layer.pack.ClientEntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.pack.EntityRenderLayers;
import net.threetag.palladium.client.renderer.entity.layer.pack.PackRenderLayer;
import net.threetag.palladium.entity.ArmSetting;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.HideModelPartAbility;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class ModelUtil {

    public static final String RIGHT_ARM_PART_NAME = "right_arm";
    public static final String LEFT_ARM_PART_NAME = "left_arm";

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
    }

    public static ModelPart getPartFromModel(Model<?> model, String name) {
        if (name.equals("root") || name.equals("*")) {
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

        for (PackRenderLayer<?> layer : ((ClientEntityRenderLayers) EntityRenderLayers.get(entity, PalladiumEntityDataTypes.RENDER_LAYERS.get())).getLayers()) {
            parts.addAll(layer.getProperties().hiddenModelParts());
        }

        return parts;
    }

    @SuppressWarnings("rawtypes")
    public static Matrix4f getInWorldModelPartMatrix(String part, Vector3f offset, AbstractClientPlayer player, float partialTicks) {
        var poseStack = new PoseStack();
        EntityRenderer<?, ?> entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);

        if (!(entityRenderer instanceof LivingEntityRenderer<?, ?, ?> livingEntityRenderer)) {
            return poseStack.last().pose();
        }

        Model<?> model = livingEntityRenderer.getModel();
        var modelPart = getPartFromModel(model, part);

        if (modelPart == null) {
            return poseStack.last().pose();
        }

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
            model.root().translateAndRotate(poseStack);
            modelPart.translateAndRotate(poseStack);
            poseStack.translate(offset.x, offset.y, offset.z);
        }

        return poseStack.last().pose();
    }

    public static Vec3 getInWorldPosition(String part, Vector3f offset, AbstractClientPlayer player, float partialTicks) {
        if (player == Minecraft.getInstance().player
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON
                && (part.equals(RIGHT_ARM_PART_NAME) || part.equals(LEFT_ARM_PART_NAME))) {
            return getInWorldFirstPersonPosition(part.equals(RIGHT_ARM_PART_NAME) ? HumanoidArm.RIGHT : HumanoidArm.LEFT, offset, partialTicks);
        }

        Vector3f vec = new Vector3f(0, 0, 0);
        vec = getInWorldModelPartMatrix(part, offset, player, partialTicks).transformPosition(vec);
        return player.getPosition(partialTicks).add(vec.x, vec.y, vec.z);
    }

    public static Vec3 getInWorldFirstPersonPosition(HumanoidArm arm, Vector3f offset, float partialTicks) {
        Vector3f vec = new Vector3f(0, 0, 0);
        vec = getInWorldFirstPersonArmMatrix(arm, offset, partialTicks).transformPosition(vec);
        return Minecraft.getInstance().gameRenderer.getMainCamera().position().add(vec.x, vec.y, vec.z);
    }

    public static Matrix4f getInWorldFirstPersonArmMatrix(HumanoidArm arm, Vector3f offset, float partialTicks) {
        var poseStack = new PoseStack();
        var mc = Minecraft.getInstance();
        var player = Objects.requireNonNull(mc.player);
        var model = ((LivingEntityRenderer<?, ?, ?>) mc.getEntityRenderDispatcher().getRenderer(player)).getModel();
        var mainArm = mc.player.getMainArm() == arm;
        var interactionHand = mainArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        var rightArm = arm == HumanoidArm.RIGHT;
        var modelPart = getPartFromModel(model, rightArm ? RIGHT_ARM_PART_NAME : LEFT_ARM_PART_NAME);

        if (modelPart == null) {
            return poseStack.last().pose();
        }

        float swingProgress = player.getAttackAnim(partialTicks);
        swingProgress = player.swingingArm == interactionHand ? swingProgress : 0.0F;
        var equippedProgress = 0F;

        // GameRenderer#renderLevel
        Quaternionf quaternionf = mc.gameRenderer.getMainCamera().rotation().conjugate(new Quaternionf());
        Matrix4f projectionMatrix = new Matrix4f().rotation(quaternionf);
        poseStack.mulPose(projectionMatrix.invert(new Matrix4f()));
        mc.gameRenderer.bobHurt(poseStack, partialTicks);
        if (mc.options.bobView().get()) {
            mc.gameRenderer.bobView(poseStack, partialTicks);
        }

        // ItemInHandRenderer#renderHandsWithItems
        float xBob = Mth.lerp(partialTicks, player.xBobO, player.xBob);
        float yBob = Mth.lerp(partialTicks, player.yBobO, player.yBob);
        poseStack.mulPose(Axis.XP.rotationDegrees((player.getViewXRot(partialTicks) - xBob) * 0.1F));
        poseStack.mulPose(Axis.YP.rotationDegrees((player.getViewYRot(partialTicks) - yBob) * 0.1F));

        // ItemInHandRenderer#renderPlayerArm
        float f = rightArm ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(swingProgress);
        float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * (float) (Math.PI * 2));
        float f4 = -0.4F * Mth.sin(swingProgress * (float) Math.PI);
        poseStack.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equippedProgress * -0.6F, f4 + -0.71999997F);
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
        float f6 = Mth.sin(f1 * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(f * f6 * 70.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(f * f5 * -20.0F));
        poseStack.translate(f * -1.0F, 3.6F, 3.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
        poseStack.translate(f * 5.6F, 0F, 0F);

        // AvatarRenderer#renderHand
//        poseStack.translate(0, 1, 0);
//        poseStack.scale(1.5F, 1.5F, 1.5F);
        modelPart.resetPose();
        modelPart.zRot = rightArm ? 0.1F : -0.1F;
        modelPart.translateAndRotate(poseStack);
        poseStack.translate(offset.x, offset.y, offset.z);

        return poseStack.last().pose();
    }

    @SubscribeEvent
    static void test(RenderArmEvent e) {
//        e.getPoseStack().translate(0, 1, 0);
//        e.getPoseStack().mulPose(Axis.XP.rotationDegrees(-130F));
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
