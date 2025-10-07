package net.threetag.palladium.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerCapeModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.IntFunction;

public enum BodyPart implements StringRepresentable {

    HEAD(0, "head", false),
    HEAD_OVERLAY(1, "head_overlay", true),
    CHEST(2, "chest", false),
    CHEST_OVERLAY(3, "chest_overlay", true),
    RIGHT_ARM(4, "right_arm", false),
    RIGHT_ARM_OVERLAY(5, "right_arm_overlay", true),
    LEFT_ARM(6, "left_arm", false),
    LEFT_ARM_OVERLAY(7, "left_arm_overlay", true),
    RIGHT_LEG(8, "right_leg", false),
    RIGHT_LEG_OVERLAY(9, "right_leg_overlay", true),
    LEFT_LEG(10, "left_leg", false),
    LEFT_LEG_OVERLAY(11, "left_leg_overlay", true),
    CAPE(12, "cape", false);

    public static final IntFunction<BodyPart> BY_ID = ByIdMap.continuous(BodyPart::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final Codec<BodyPart> CODEC = StringRepresentable.fromEnum(BodyPart::values);
    public static final StreamCodec<ByteBuf, BodyPart> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, BodyPart::id);

    private final int id;
    private final String name;
    private final boolean overlay;

    BodyPart(int id, String name, boolean overlay) {
        this.id = id;
        this.name = name;
        this.overlay = overlay;
    }

    public int id() {
        return id;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public boolean isOverlay() {
        return this.overlay;
    }

    @Nullable
    @Environment(EnvType.CLIENT)
    public ModelPart getModelPart(HumanoidModel<?> model) {
        PlayerModel playerModel = model instanceof PlayerModel pl ? pl : null;

        return switch (this) {
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
            case CAPE -> model instanceof PlayerCapeModel<?> capeModel ? capeModel.cape : null;
        };
    }

    @Nullable
    public BodyPart getOverlayPart() {
        return switch (this) {
            case HEAD -> HEAD_OVERLAY;
            case CHEST -> CHEST_OVERLAY;
            case RIGHT_ARM -> RIGHT_ARM_OVERLAY;
            case LEFT_ARM -> LEFT_ARM_OVERLAY;
            case RIGHT_LEG -> RIGHT_LEG_OVERLAY;
            case LEFT_LEG -> LEFT_LEG_OVERLAY;
            default -> null;
        };
    }

    @Environment(EnvType.CLIENT)
    public void setVisibility(HumanoidModel<?> model, boolean visible) {
        ModelPart part = getModelPart(model);

        if (part != null) {
            part.visible = visible;
        }
    }

    public static BodyPart byName(String name) {
        for (BodyPart bodyPart : values()) {
            if (name.equalsIgnoreCase(bodyPart.name)) {
                return bodyPart;
            }
        }

        return null;
    }

    public static Set<BodyPart> getHiddenBodyParts(LivingEntity entity) {
        Set<BodyPart> parts = new HashSet<>();

        // Customizations
        for (Holder<Customization> customization : EntityCustomizationHandler.get(entity).getSelected()) {
            parts.addAll(customization.value().getHiddenBodyParts());
        }

        return parts;
    }

    public static Set<BodyPart> getRemovedBodyParts(LivingEntity entity) {
        return Collections.emptySet();
    }

    @SuppressWarnings("rawtypes")
    @Environment(EnvType.CLIENT)
    public static Matrix4f getTransformationMatrix(BodyPart part, Vector3f offset, HumanoidModel<?> model, AbstractClientPlayer player, float partialTicks) {
        var poseStack = new PoseStack();
        var modelPart = part.getModelPart(model);

        if (modelPart == null) {
            return poseStack.last().pose();
        }

        EntityRenderer entityRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);

        if (entityRenderer instanceof PlayerRenderer renderer && renderer.createRenderState(player, partialTicks) instanceof PlayerRenderState state) {
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

    @Environment(EnvType.CLIENT)
    public static Matrix4f getTransformationMatrix(BodyPart part, Vector3f offset, AbstractClientPlayer player, float partialTicks) {
        if (player instanceof PlayerModelCacheExtension ext) {
            return getTransformationMatrix(part, offset, ext.palladium$getCachedModel(), player, partialTicks);
        } else {
            return new Matrix4f();
        }
    }

    @Environment(EnvType.CLIENT)
    public static Vec3 getInWorldPosition(BodyPart part, Vector3f offset, HumanoidModel<?> model, AbstractClientPlayer player, float partialTicks) {
        Vector3f vec = new Vector3f(0, 0, 0);
        vec = getTransformationMatrix(part, offset, model, player, partialTicks).transformPosition(vec);
        return player.getPosition(partialTicks).add(vec.x, vec.y, vec.z);
    }

    @Environment(EnvType.CLIENT)
    public static Vec3 getInWorldPosition(BodyPart part, Vector3f offset, AbstractClientPlayer player, float partialTicks) {
        if (player instanceof PlayerModelCacheExtension ext) {
            return getInWorldPosition(part, offset, ext.palladium$getCachedModel(), player, partialTicks);
        } else {
            return player.getPosition(partialTicks);
        }
    }

}
