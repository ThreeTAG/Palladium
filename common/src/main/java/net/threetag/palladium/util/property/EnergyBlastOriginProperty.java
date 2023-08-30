package net.threetag.palladium.util.property;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.renderlayer.AbilityEffectsRenderLayer;
import net.threetag.palladium.util.EntityUtil;
import net.threetag.palladium.util.MathUtil;
import net.threetag.palladium.util.RenderUtil;
import net.threetag.palladium.util.SizeUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class EnergyBlastOriginProperty extends EnumPalladiumProperty<EnergyBlastOriginProperty.EnergyBlastOrigin> {

    public EnergyBlastOriginProperty(String key) {
        super(key);
    }

    @Override
    public EnergyBlastOrigin[] getValues() {
        return EnergyBlastOrigin.values();
    }

    @Override
    public String getNameFromEnum(EnergyBlastOrigin value) {
        return value.getName();
    }

    public enum EnergyBlastOrigin {

        EYES("eyes"),
        FOREHEAD("forehead"),
        CHEST("chest");

        private final String name;

        EnergyBlastOrigin(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public Vec3 getOriginVector(LivingEntity entity) {
            if (this == EYES) {
                return entity.position().add(0, entity.getEyeHeight(), 0);
            } else if (this == FOREHEAD) {
                Vec3 startVec = entity.position().add(0, entity.getEyeHeight(), 0);
                Vec3 forehead = new Vec3(0, 2F / 16F * 0.9375F * SizeUtil.getInstance().getHeightScale(entity), 0)
                        .xRot((float) Math.toRadians(-entity.getXRot())).yRot((float) Math.toRadians(-entity.getYRot()));
                return startVec.add(forehead);
            } else if (this == CHEST) {
                return entity.position().add(0, 17F / 16F * 0.9375F * SizeUtil.getInstance().getHeightScale(entity), 0);
            }
            return null;
        }

        public Vec3 getEndVector(LivingEntity entity, Vec3 startVec, double distance) {
            if (this == CHEST) {
                return startVec.add(MathUtil.calculateViewVector(entity.isCrouching() ? (float) Math.toDegrees(0.5F) : 0, entity.yBodyRot).scale(distance));
            }
            return startVec.add(EntityUtil.getLookVector(entity).scale(distance));
        }

        @Environment(EnvType.CLIENT)
        public void render(PoseStack poseStack, VertexConsumer vertexConsumer, LivingEntity entity, Color color, @Nullable AbilityEffectsRenderLayer layer, double distance, float partialTicks) {
            if (this == EYES || this == FOREHEAD) {
                float scale = 1F / 16F;

                if (layer != null) {
                    distance /= SizeUtil.getInstance().getWidthScale(entity, partialTicks);
                    ((HeadedModel) layer.getParentModel()).getHead().translateAndRotate(poseStack);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                } else {
                    float yaw = entity.getViewYRot(partialTicks);
                    float pitch = entity.getViewXRot(partialTicks);

                    poseStack.translate(0, entity.getEyeHeight(), 0);
                    poseStack.mulPose(Axis.YN.rotationDegrees(yaw));
                    poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                }

                if (this == EYES) {
                    float distanceFromCenter = 0.15F;

                    poseStack.translate(layer == null ? distanceFromCenter : scale * 1.5F, 0, layer != null ? scale * 3.5F : 0);
                    RenderUtil.drawGlowingBox(poseStack, vertexConsumer, (float) distance, scale / 2F, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F, 15728640);
                    poseStack.translate(layer == null ? -2F * distanceFromCenter : scale * -3F, 0, 0);
                    RenderUtil.drawGlowingBox(poseStack, vertexConsumer, (float) distance, scale / 2F, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F, 15728640);
                } else {
                    poseStack.translate(0, 0, layer != null ? scale * 5F : scale * -1.5F);
                    RenderUtil.drawGlowingBox(poseStack, vertexConsumer, (float) distance, scale / 2F, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F, 15728640);
                }
            } else if (this == CHEST) {
                if (layer != null) {
                    distance /= SizeUtil.getInstance().getWidthScale(entity, partialTicks);
                    ((HumanoidModel<LivingEntity>) layer.getParentModel()).body.translateAndRotate(poseStack);
                    poseStack.translate(0, 3F / 16F, 0);
                    poseStack.mulPose(Axis.YP.rotationDegrees(180));
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                } else {
                    float yaw = Mth.lerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
                    float pitch = entity.isCrouching() ? 0.5F : 0;
                    poseStack.translate(0, 17F / 16F * 0.9375F * SizeUtil.getInstance().getHeightScale(entity, partialTicks), 0);
                    poseStack.mulPose(Axis.YN.rotationDegrees(yaw));
                    poseStack.mulPose(Axis.XP.rotation(pitch));
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                }
                RenderUtil.drawGlowingBox(poseStack, vertexConsumer, (float) distance, 2F / 16F, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F, 15728640);
            }
        }

    }

}
