package net.threetag.palladium.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.model.animation.PalladiumAnimationRegistry;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.mixin.client.AgeableListModelInvoker;
import net.threetag.palladium.power.ability.ShrinkBodyOverlayAbility;

@SuppressWarnings({"rawtypes"})
public class HumanoidRendererModifications {

    private static float CACHED_SHRINK = 0F;
    public static BodyPart.ModifiedBodyPartResult CACHED_HIDE_RESULT = null;
    public static float CACHED_LIMB_SWING, CACHED_LIMB_SWING_AMOUNT, CACHED_AGE_IN_TICKS, CACHED_NET_HEAD_YAW, CACHED_HEAD_PITCH;

    public static void preSetup(LivingEntityRenderer renderer, LivingEntity entity, HumanoidModel model, PoseStack poseStack, float partialTicks) {
        // reset poses
        if(model instanceof AgeableListModelInvoker invoker) {
            PalladiumAnimationRegistry.resetPoses(invoker.invokeHeadParts(), invoker.invokeBodyParts());
        }

        // rotate player model
        if (renderer instanceof PlayerRenderer playerRenderer && entity instanceof AbstractClientPlayer player) {
            PalladiumAnimationRegistry.setupRotations(playerRenderer, player, poseStack, partialTicks);
        }
    }

    public static void preRender(LivingEntityRenderer renderer, LivingEntity entity, HumanoidModel model, PoseStack poseStack) {
        // animations
        if (!PalladiumAnimationRegistry.SKIP_ANIMATIONS) {
            PalladiumAnimationRegistry.applyAnimations(model, entity, CACHED_LIMB_SWING, CACHED_LIMB_SWING_AMOUNT, CACHED_AGE_IN_TICKS, CACHED_NET_HEAD_YAW, CACHED_HEAD_PITCH);
        }

        // visibility
        BodyPart.resetBodyParts(entity, model);
        CACHED_HIDE_RESULT = BodyPart.getModifiedBodyParts(entity, false);
        BodyPart.hideHiddenOrRemovedParts(model, entity, CACHED_HIDE_RESULT);

        // layer shrinking
        float scale = ShrinkBodyOverlayAbility.getValue(entity);

        if (scale != 0F) {
            float f = -0.1F * scale;
            CACHED_SHRINK = f;
            Vector3f vec = new Vector3f(f, f, f);
            for (BodyPart value : BodyPart.values()) {
                ModelPart part = value.getModelPart(model);
                if (value.isOverlay() && part != null) {
                    part.offsetScale(vec);
                }
            }
        }
    }

    public static void preLayers(LivingEntityRenderer renderer, LivingEntity entity, HumanoidModel model, PoseStack poseStack) {
        // reset layer-shrink
        if (CACHED_SHRINK != 0F) {
            float f = -CACHED_SHRINK;
            CACHED_SHRINK = 0F;
            Vector3f vec = new Vector3f(f, f, f);
            for (BodyPart value : BodyPart.values()) {
                ModelPart part = value.getModelPart(model);
                if (value.isOverlay() && part != null) {
                    part.offsetScale(vec);
                }
            }
        }
    }

    public static void applyRemovedBodyParts(HumanoidModel model) {
        var hideResult = HumanoidRendererModifications.CACHED_HIDE_RESULT;

        if (hideResult.isRemoved(BodyPart.HEAD))
            model.head.visible = model.hat.visible = false;
        if (hideResult.isRemoved(BodyPart.CHEST))
            model.body.visible = false;
        if (hideResult.isRemoved(BodyPart.RIGHT_ARM))
            model.rightArm.visible = false;
        if (hideResult.isRemoved(BodyPart.LEFT_ARM))
            model.leftArm.visible = false;
        if (hideResult.isRemoved(BodyPart.RIGHT_LEG))
            model.rightLeg.visible = false;
        if (hideResult.isRemoved(BodyPart.LEFT_LEG))
            model.leftLeg.visible = false;
    }

}
