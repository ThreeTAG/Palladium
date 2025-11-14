package net.threetag.palladium.compat.geckolib.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.logic.context.DataContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderModelPositioner;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"rawtypes", "unchecked", "UnstableApiUsage"})
public class GeoRenderLayerRenderer<R extends HumanoidRenderState & GeoRenderState> implements GeoRenderer<GeoRenderLayerState, DataContext, R> {

    protected final GeoRenderLayersContainer<GeoRenderLayerState, DataContext, R> renderLayers = new GeoRenderLayersContainer<>(this);
    protected final GeoModel<GeoRenderLayerState> model;

    protected float scaleWidth = 1;
    protected float scaleHeight = 1;

    protected final Map<net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone, String> bones;

    public GeoRenderLayerRenderer(Map<net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone, String> bones, GeoModel<GeoRenderLayerState> model) {
        this.model = model;
        this.bones = bones;
    }

    /**
     * Gets the model instance for this renderer
     */
    @Override
    public GeoModel<GeoRenderLayerState> getGeoModel() {
        return this.model;
    }

    /**
     * Returns the list of registered {@link GeoRenderLayer GeoRenderLayers} for this renderer
     */
    @Override
    public List<GeoRenderLayer<GeoRenderLayerState, DataContext, R>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    public GeoRenderLayerRenderer<R> addRenderLayer(GeoRenderLayer<GeoRenderLayerState, DataContext, R> renderLayer) {
        this.renderLayers.addLayer(renderLayer);
        return this;
    }

    /**
     * Sets a scale override for this renderer, telling GeckoLib to pre-scale the model
     */
    public GeoRenderLayerRenderer<R> withScale(float scale) {
        return withScale(scale, scale);
    }

    /**
     * Sets a scale override for this renderer, telling GeckoLib to pre-scale the model
     */
    public GeoRenderLayerRenderer<R> withScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;

        return this;
    }

    @Override
    public long getInstanceId(GeoRenderLayerState animatable, DataContext relatedObject) {
        return GeoRenderer.super.getInstanceId(animatable, relatedObject);
    }

    @Nullable
    @Override
    public RenderType getRenderType(R renderState, ResourceLocation texture) {
        return RenderType.armorCutoutNoCull(texture);
    }

    @Override
    public R captureDefaultRenderState(GeoRenderLayerState animatable, DataContext context, R renderState, float partialTick) {
        GeoRenderer.super.captureDefaultRenderState(animatable, context, renderState, partialTick);

        var slot = context.getSlot();
        var item = context.getItem();

        renderState.addGeckolibData(DataTickets.IS_GECKOLIB_WEARER, context.getEntity() instanceof GeoAnimatable);
        if (slot != null)
            renderState.addGeckolibData(DataTickets.EQUIPMENT_SLOT, slot.getEquipmentSlot());
        if (!item.isEmpty()) {
            renderState.addGeckolibData(DataTickets.HAS_GLINT, item.hasFoil());
            renderState.addGeckolibData(DataTickets.IS_ENCHANTED, item.isEnchanted());
            renderState.addGeckolibData(DataTickets.IS_STACKABLE, item.isStackable());
        }
        renderState.addGeckolibData(DataTickets.INVISIBLE_TO_PLAYER, renderState.isInvisibleToPlayer);

        return renderState;
    }

    @Override
    public R createRenderState(GeoRenderLayerState animatable, DataContext relatedObject) {
        return (R) new HumanoidRenderState();
    }

    @Override
    public void preRender(R renderState, PoseStack poseStack, BakedGeoModel model, SubmitNodeCollector renderTasks, CameraRenderState cameraState,
                          int packedLight, int packedOverlay, int renderColor) {
        renderState.addGeckolibData(DataTickets.OBJECT_RENDER_POSE, new Matrix4f(poseStack.last().pose()));
    }

    @Override
    public void scaleModelForRender(R renderState, float widthScale, float heightScale, PoseStack poseStack, BakedGeoModel model, CameraRenderState cameraState) {
        GeoRenderer.super.scaleModelForRender(renderState, widthScale * this.scaleWidth, heightScale * this.scaleHeight, poseStack, model, cameraState);
    }

    @Override
    public void adjustRenderPose(R renderState, PoseStack poseStack, BakedGeoModel model, CameraRenderState cameraState) {
        poseStack.translate(0, 24 / 16f, 0);
        poseStack.scale(-1, -1, 1);
        renderState.addGeckolibData(DataTickets.MODEL_RENDER_POSE, new Matrix4f(poseStack.last().pose()));
    }

    @Override
    public void fireCompileRenderLayersEvent() {

    }

    @Override
    public void fireCompileRenderStateEvent(GeoRenderLayerState animatable, DataContext relatedObject, R renderState, float partialTick) {

    }

    @Override
    public boolean firePreRenderEvent(R renderState, PoseStack poseStack, BakedGeoModel model, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {
        return false;
    }

    @Override
    public void firePostRenderEvent(R renderState, PoseStack poseStack, BakedGeoModel model, SubmitNodeCollector renderTasks, CameraRenderState cameraState) {

    }

    @Override
    public void buildRenderTask(R renderState, PoseStack poseStack, BakedGeoModel bakedModel, GeoModel<GeoRenderLayerState> model, OrderedSubmitNodeCollector renderTasks, CameraRenderState cameraState, @Nullable RenderType renderType, int packedLight, int packedOverlay, int renderColor, @Nullable RenderModelPositioner<R> modelPositioner) {
        if (renderType == null)
            return;

        renderTasks.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
            final EquipmentSlot slot = renderState.getGeckolibData(DataTickets.EQUIPMENT_SLOT);
            final PoseStack poseStack2 = new PoseStack();
            final HumanoidModel baseModel = renderState.getGeckolibData(DataTickets.HUMANOID_MODEL);

            poseStack2.last().set(pose);

            for (ArmorSegment segment : getSegmentsForSlot(renderState, slot)) {
                model.getBone(getBoneNameForSegment(renderState, segment)).ifPresent(bone -> {
                    ModelPart modelPart = segment.modelPartGetter.apply(baseModel);
                    Vector3f bonePos = segment.modelPartMatcher.apply(new Vector3f(modelPart.x, modelPart.y, modelPart.z));

                    baseModel.setupAnim(renderState);
                    RenderUtil.matchModelPartRot(modelPart, bone);
                    bone.updatePosition(bonePos.x, bonePos.y, bonePos.z);

                    renderBone(renderState, poseStack2, bone, vertexConsumer, cameraState, packedLight, packedOverlay, renderColor);
                });
            }
        });
    }

    public String getBoneNameForSegment(R renderState, ArmorSegment segment) {
        return switch (segment) {
            case HEAD -> "head";
            case CHEST -> "body";
            case LEFT_ARM -> "left_arm";
            case RIGHT_ARM -> "right_arm";
            case LEFT_LEG -> "left_leg";
            case RIGHT_LEG -> "right_leg";
        };
    }

    public List<ArmorSegment> getSegmentsForSlot(R renderState, EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> List.of(ArmorSegment.HEAD);
            case CHEST -> List.of(ArmorSegment.CHEST, ArmorSegment.LEFT_ARM, ArmorSegment.RIGHT_ARM);
            case LEGS, FEET -> List.of(ArmorSegment.LEFT_LEG, ArmorSegment.RIGHT_LEG);
            default -> List.of();
        };
    }

    public enum ArmorSegment {
        HEAD(EquipmentSlot.HEAD, model -> model.head, pos -> pos.mul(1, -1, 1)),
        CHEST(EquipmentSlot.CHEST, model -> model.body, pos -> pos.mul(1, -1, 1)),
        LEFT_ARM(EquipmentSlot.CHEST, model -> model.leftArm, pos -> pos.set(pos.x - 5, 2 - pos.y, pos.z)),
        RIGHT_ARM(EquipmentSlot.CHEST, model -> model.rightArm, pos -> pos.set(pos.x + 5, 2 - pos.y, pos.z)),
        LEFT_LEG(EquipmentSlot.LEGS, model -> model.leftLeg, pos -> pos.set(pos.x - 2, 12 - pos.y, pos.z)),
        RIGHT_LEG(EquipmentSlot.LEGS, model -> model.rightLeg, pos -> pos.set(pos.x + 2, 12 - pos.y, pos.z));

        public final EquipmentSlot equipmentSlot;
        public final Function<HumanoidModel<?>, ModelPart> modelPartGetter;
        public final UnaryOperator<Vector3f> modelPartMatcher;

        ArmorSegment(EquipmentSlot slot, Function<HumanoidModel<?>, ModelPart> modelPartGetter, UnaryOperator<Vector3f> modelPartMatcher) {
            this.equipmentSlot = slot;
            this.modelPartGetter = modelPartGetter;
            this.modelPartMatcher = modelPartMatcher;
        }
    }


}
