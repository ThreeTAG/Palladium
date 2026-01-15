package net.threetag.palladium.compat.geckolib.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.logic.context.DataContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings({"unchecked", "UnstableApiUsage"})
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
    public RenderType getRenderType(R renderState, Identifier texture) {
        return RenderTypes.armorCutoutNoCull(texture);
    }

    @Override
    public void captureDefaultRenderState(GeoRenderLayerState animatable, @org.jspecify.annotations.Nullable DataContext relatedObject, R renderState, float partialTick) {
        GeoRenderer.super.captureDefaultRenderState(animatable, relatedObject, renderState, partialTick);
        var slot = relatedObject.getSlot();
        var item = relatedObject.getItem();

        renderState.addGeckolibData(DataTickets.TICK, (double) renderState.ageInTicks);
        renderState.addGeckolibData(DataTickets.POSITION, relatedObject.getEntity().position());
        renderState.addGeckolibData(DataTickets.IS_GECKOLIB_WEARER, relatedObject.getEntity() instanceof GeoAnimatable);
        if (slot != null)
            renderState.addGeckolibData(DataTickets.EQUIPMENT_SLOT, slot.getEquipmentSlot());
        renderState.addGeckolibData(DataTickets.HAS_GLINT, item.hasFoil());
        renderState.addGeckolibData(DataTickets.INVISIBLE_TO_PLAYER, renderState.isInvisibleToPlayer);
    }

    @Override
    public R createRenderState(GeoRenderLayerState animatable, DataContext relatedObject) {
        return (R) new HumanoidRenderState();
    }

    @Override
    public void scaleModelForRender(RenderPassInfo<R> renderPassInfo, float widthScale, float heightScale) {
        GeoRenderer.super.scaleModelForRender(renderPassInfo, this.scaleWidth * widthScale, this.scaleHeight * heightScale);
    }

    @Override
    public void adjustRenderPose(RenderPassInfo<R> renderPassInfo) {
        renderPassInfo.poseStack().translate(0, 24 / 16f, 0);
        renderPassInfo.poseStack().scale(-1, -1, 1);
    }

    @Override
    public void fireCompileRenderLayersEvent() {

    }

    @Override
    public void fireCompileRenderStateEvent(GeoRenderLayerState animatable, DataContext relatedObject, R renderState, float partialTick) {

    }

    @Override
    public boolean firePreRenderEvent(RenderPassInfo<R> renderPassInfo, SubmitNodeCollector renderTasks) {
        return true;
    }

    @Override
    public void submitRenderTasks(RenderPassInfo<R> renderPassInfo, OrderedSubmitNodeCollector renderTasks, @org.jspecify.annotations.Nullable RenderType renderType) {
        if (renderType == null)
            return;

        renderTasks.submitCustomGeometry(renderPassInfo.poseStack(), renderType, (pose, vertexConsumer) -> {
            final PoseStack poseStack = renderPassInfo.poseStack();
            final R renderState = renderPassInfo.renderState();
            final int packedLight = renderPassInfo.packedLight();
            final int packedOverlay = renderPassInfo.packedOverlay();
            final int renderColor = renderPassInfo.renderColor();
            final EquipmentSlot slot = Objects.requireNonNull(renderState.getGeckolibData(DataTickets.EQUIPMENT_SLOT));

            poseStack.pushPose();
            poseStack.last().set(pose);
            renderPassInfo.renderPosed(() -> {
                for (ArmorSegment segment : getSegmentsForSlot(renderState, slot)) {
                    renderPassInfo.model().getBone(getBoneNameForSegment(renderState, segment))
                            .ifPresent(bone -> bone.positionAndRender(renderPassInfo, vertexConsumer, packedLight, packedOverlay, renderColor));
                }
            });
            poseStack.popPose();
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
