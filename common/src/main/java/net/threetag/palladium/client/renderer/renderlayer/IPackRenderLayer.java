package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.threetag.palladium.client.renderer.entity.HumanoidRendererModifications;
import net.threetag.palladium.condition.*;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.mixin.client.AgeableListModelInvoker;
import net.threetag.palladium.util.context.DataContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface IPackRenderLayer {

    void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    default void renderArm(DataContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

    }

    IPackRenderLayer addCondition(Condition condition, PerspectiveConditionContext context);

    List<BodyPart> getHiddenBodyParts(LivingEntity entity);

    default void onLoad() {

    }

    default void onUnload() {

    }

    default boolean isOrContains(IPackRenderLayer layer) {
        return this == layer;
    }

    default RenderLayerStates.State createState() {
        return null;
    }

    default void createSnapshot(DataContext context, EntityModel<Entity> parentModel, Consumer<Snapshot> consumer) {

    }

    static <T extends IPackRenderLayer> T parseConditions(T layer, JsonObject json) {
        for (PerspectiveConditionContext context : PerspectiveConditionContext.values()) {
            if (GsonHelper.isValidNode(json, context.key)) {
                var el = json.get(context.key);

                if (el.isJsonPrimitive()) {
                    var result = el.getAsBoolean();
                    layer.addCondition(result ? new TrueCondition() : new FalseCondition(), context);
                } else {
                    ConditionSerializer.listFromJSON(el, ConditionEnvironment.ASSETS).forEach(cond -> layer.addCondition(cond, context));
                }
            }
        }

        return layer;
    }

    static boolean conditionsFulfilled(Entity entity, List<Condition> bothConditions, List<Condition> specificConditions) {
        if (entity instanceof LivingEntity livingEntity) {
            for (Condition condition : bothConditions) {
                if (!condition.active(DataContext.forEntity(livingEntity))) {
                    return false;
                }
            }

            for (Condition condition : specificConditions) {
                if (!condition.active(DataContext.forEntity(livingEntity))) {
                    return false;
                }
            }
        }

        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static void copyModelProperties(Entity entity, HumanoidModel parent, HumanoidModel child) {
        parent.copyPropertiesTo(child);

        if (entity instanceof ArmorStand armorStand) {
            child.head.xRot = 0.017453292F * armorStand.getHeadPose().getX();
            child.head.yRot = 0.017453292F * armorStand.getHeadPose().getY();
            child.head.zRot = 0.017453292F * armorStand.getHeadPose().getZ();
            child.body.xRot = 0.017453292F * armorStand.getBodyPose().getX();
            child.body.yRot = 0.017453292F * armorStand.getBodyPose().getY();
            child.body.zRot = 0.017453292F * armorStand.getBodyPose().getZ();
            child.leftArm.xRot = 0.017453292F * armorStand.getLeftArmPose().getX();
            child.leftArm.yRot = 0.017453292F * armorStand.getLeftArmPose().getY();
            child.leftArm.zRot = 0.017453292F * armorStand.getLeftArmPose().getZ();
            child.rightArm.xRot = 0.017453292F * armorStand.getRightArmPose().getX();
            child.rightArm.yRot = 0.017453292F * armorStand.getRightArmPose().getY();
            child.rightArm.zRot = 0.017453292F * armorStand.getRightArmPose().getZ();
            child.leftLeg.xRot = 0.017453292F * armorStand.getLeftLegPose().getX();
            child.leftLeg.yRot = 0.017453292F * armorStand.getLeftLegPose().getY();
            child.leftLeg.zRot = 0.017453292F * armorStand.getLeftLegPose().getZ();
            child.rightLeg.xRot = 0.017453292F * armorStand.getRightLegPose().getX();
            child.rightLeg.yRot = 0.017453292F * armorStand.getRightLegPose().getY();
            child.rightLeg.zRot = 0.017453292F * armorStand.getRightLegPose().getZ();
            child.hat.copyFrom(child.head);
        }

        child.setAllVisible(true);
        HumanoidRendererModifications.applyRemovedBodyParts(child);
    }

    enum PerspectiveConditionContext {

        BOTH("conditions"),
        FIRST_PERSON("first_person_conditions"),
        THIRD_PERSON("third_person_conditions");

        public final String key;

        PerspectiveConditionContext(String key) {
            this.key = key;
        }
    }

    @SuppressWarnings("rawtypes")
    class Snapshot {

        private final EntityModel<?> model;
        private final ResourceLocation texture;
        private final Map<ModelPart, PartPose> poseMap = new HashMap<>();

        public Snapshot(EntityModel<?> model, ResourceLocation texture) {
            this.model = model;
            this.texture = texture;

            if (model instanceof AgeableListModelInvoker ageable) {
                for (ModelPart part : ageable.invokeHeadParts()) {
                    this.storePose(part);
                }

                for (ModelPart part : ageable.invokeBodyParts()) {
                    this.storePose(part);
                }
            } else if (model instanceof HierarchicalModel hierarchical) {
                for (ModelPart part : hierarchical.root().children.values()) {
                    this.storePose(part);
                }
            }
        }

        private void storePose(ModelPart part) {
            this.poseMap.put(part, part.storePose());
            for (ModelPart child : part.children.values()) {
                storePose(child);
            }
        }

        public void applyPoses() {
            for (Map.Entry<ModelPart, PartPose> e : this.poseMap.entrySet()) {
                e.getKey().loadPose(e.getValue());
            }
        }

        public EntityModel<?> getModel() {
            return model;
        }

        public ResourceLocation getTexture() {
            return texture;
        }
    }

}
