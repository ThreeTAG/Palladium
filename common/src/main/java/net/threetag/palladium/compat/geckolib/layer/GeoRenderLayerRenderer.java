package net.threetag.palladium.compat.geckolib.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.threetag.palladium.data.DataContext;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.processing.AnimationProcessor;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;
import software.bernie.geckolib.util.RenderUtil;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("rawtypes")
public class GeoRenderLayerRenderer<R extends LivingEntityRenderState & GeoRenderState> extends HumanoidModel implements GeoRenderer<GeoRenderLayerState, DataContext, R> {

    protected final GeoRenderLayersContainer<GeoRenderLayerState, DataContext, R> renderLayers = new GeoRenderLayersContainer<>(this);
    protected final GeoModel<GeoRenderLayerState> model;

    protected float scaleWidth = 1;
    protected float scaleHeight = 1;

    protected Matrix4f entityRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();

    protected final Map<net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone, String> bones;
    protected BakedGeoModel lastModel = null;
    protected GeoBone headBone = null;
    protected GeoBone bodyBone = null;
    protected GeoBone rightArmBone = null;
    protected GeoBone leftArmBone = null;
    protected GeoBone rightLegBone = null;
    protected GeoBone leftLegBone = null;

    public GeoRenderLayerRenderer(Map<net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone, String> bones, GeoModel<GeoRenderLayerState> model) {
        super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
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
        renderState.addGeckolibData(DataTickets.IS_GLOWING, renderState.appearsGlowing);

        return renderState;
    }

    @Override
    public void preRender(R renderState, PoseStack poseStack, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer,
                          boolean isReRender, int packedLight, int packedOverlay, int renderColor) {
        if (!isReRender) {
            this.entityRenderTranslations = new Matrix4f(poseStack.last().pose());

            applyBaseTransformations(renderState.getGeckolibData(DataTickets.HUMANOID_MODEL));
            applyBaseModel(renderState.getGeckolibData(DataTickets.HUMANOID_MODEL));
            grabRelevantBones(model);

            if (!renderState.getGeckolibData(DataTickets.IS_GECKOLIB_WEARER))
                applyBoneVisibilityBySlot(renderState.getGeckolibData(DataTickets.EQUIPMENT_SLOT));
        }
    }

    @Override
    public void scaleModelForRender(R renderState, float widthScale, float heightScale, PoseStack poseStack, BakedGeoModel model, boolean isReRender) {
        GeoRenderer.super.scaleModelForRender(renderState, widthScale * this.scaleWidth, heightScale * this.scaleHeight, poseStack, model, isReRender);
    }

    @Override
    public void fireCompileRenderLayersEvent() {

    }

    @Override
    public void fireCompileRenderStateEvent(GeoRenderLayerState animatable, DataContext relatedObject, R renderState) {

    }

    @Override
    public boolean firePreRenderEvent(R renderState, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource) {
        return true;
    }

    @Override
    public void firePostRenderEvent(R renderState, PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource) {

    }

    @Override
    public void actuallyRender(R renderState, PoseStack poseStack, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, int packedLight, int packedOverlay, int renderColor) {
        poseStack.pushPose();
        poseStack.translate(0, 24 / 16f, 0);
        poseStack.scale(-1, -1, 1);

        if (!isReRender)
            getGeoModel().handleAnimations(createAnimationState(renderState));

        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

        if (buffer != null)
            GeoRenderer.super.actuallyRender(renderState, poseStack, model, renderType, bufferSource, buffer, isReRender, packedLight, packedOverlay, renderColor);

        poseStack.popPose();
    }

    @Override
    public void renderRecursively(R renderState, PoseStack poseStack, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender,
                                  int packedLight, int packedOverlay, int renderColor) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());

            bone.setModelSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(RenderUtil.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations));
        }

        GeoRenderer.super.renderRecursively(renderState, poseStack, bone, renderType, bufferSource, buffer, isReRender, packedLight, packedOverlay, renderColor);
    }

    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel == bakedModel)
            return;

        AnimationProcessor<GeoRenderLayerState> animationProcessor = this.model.getAnimationProcessor();
        this.lastModel = bakedModel;
        this.headBone = this.getBone(animationProcessor, net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone.HEAD);
        this.bodyBone = this.getBone(animationProcessor, net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone.BODY);
        this.rightArmBone = this.getBone(animationProcessor, net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone.RIGHT_ARM);
        this.leftArmBone = this.getBone(animationProcessor, net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone.LEFT_ARM);
        this.rightLegBone = this.getBone(animationProcessor, net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone.RIGHT_LEG);
        this.leftLegBone = this.getBone(animationProcessor, net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone.LEFT_LEG);
    }

    public GeoBone getBone(AnimationProcessor<GeoRenderLayerState> processor, net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer.Bone bone) {
        return model.getBone(this.bones.getOrDefault(bone, bone.getSerializedName())).orElse(null);
    }

    protected void applyBaseModel(HumanoidModel<?> baseModel) {
        HumanoidModel<?> self = (HumanoidModel<?>)this;

        self.head.visible = baseModel.head.visible;
        self.hat.visible = baseModel.hat.visible;
        self.body.visible = baseModel.body.visible;
        self.rightArm.visible = baseModel.rightArm.visible;
        self.leftArm.visible = baseModel.leftArm.visible;
        self.rightLeg.visible = baseModel.rightLeg.visible;
        self.leftLeg.visible = baseModel.leftLeg.visible;
    }

    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        setAllBonesVisible(false);

        switch (currentSlot) {
            case HEAD -> setBonesVisible(this.head.visible, this.headBone);
            case CHEST -> setBonesVisible(this.body.visible, this.bodyBone, this.rightArmBone, this.leftArmBone);
            case LEGS -> setBonesVisible(this.rightLeg.visible, this.bodyBone, this.rightLegBone, this.leftLegBone);
            case FEET -> setBonesVisible(this.rightLeg.visible, this.rightLegBone, this.leftLegBone);
            default -> {}
        }
    }

    @Override
    public void setAllVisible(boolean visible) {
        super.setAllVisible(visible);
        setAllBonesVisible(visible);
    }

    protected void setAllBonesVisible(boolean visible) {
        setBonesVisible(visible, this.headBone, this.bodyBone, this.rightArmBone, this.leftArmBone,
                this.rightLegBone, this.leftLegBone);
    }

    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        setAllVisible(false);

        currentPart.visible = true;
        GeoBone bone = null;

        if (currentPart == model.hat || currentPart == model.head) {
            bone = this.headBone;
        }
        else if (currentPart == model.body) {
            bone = this.bodyBone;
        }
        else if (currentPart == model.leftArm) {
            bone = this.leftArmBone;
        }
        else if (currentPart == model.rightArm) {
            bone = this.rightArmBone;
        }
        else if (currentPart == model.leftLeg) {
            bone = this.leftLegBone;
        }
        else if (currentPart == model.rightLeg) {
            bone = this.rightLegBone;
        }

        if (bone != null)
            bone.setHidden(false);
    }

    /**
     * Transform the currently rendering {@link GeoModel} to match the positions and rotations of the base model
     */
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        if (this.headBone != null) {
            ModelPart headPart = baseModel.head;

            RenderUtil.matchModelPartRot(headPart, this.headBone);
            this.headBone.updatePosition(headPart.x, -headPart.y, headPart.z);
        }

        if (this.bodyBone != null) {
            ModelPart bodyPart = baseModel.body;

            RenderUtil.matchModelPartRot(bodyPart, this.bodyBone);
            this.bodyBone.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }

        if (this.rightArmBone != null) {
            ModelPart rightArmPart = baseModel.rightArm;

            RenderUtil.matchModelPartRot(rightArmPart, this.rightArmBone);
            this.rightArmBone.updatePosition(rightArmPart.x + 5, 2 - rightArmPart.y, rightArmPart.z);
        }

        if (this.leftArmBone != null) {
            ModelPart leftArmPart = baseModel.leftArm;

            RenderUtil.matchModelPartRot(leftArmPart, this.leftArmBone);
            this.leftArmBone.updatePosition(leftArmPart.x - 5f, 2f - leftArmPart.y, leftArmPart.z);
        }

        if (this.rightLegBone != null) {
            ModelPart rightLegPart = baseModel.rightLeg;

            RenderUtil.matchModelPartRot(rightLegPart, this.rightLegBone);
            this.rightLegBone.updatePosition(rightLegPart.x + 2, 12 - rightLegPart.y, rightLegPart.z);
        }

        if (this.leftLegBone != null) {
            ModelPart leftLegPart = baseModel.leftLeg;

            RenderUtil.matchModelPartRot(leftLegPart, this.leftLegBone);
            this.leftLegBone.updatePosition(leftLegPart.x - 2, 12 - leftLegPart.y, leftLegPart.z);
        }
    }

    public static <S extends HumanoidRenderState, M extends HumanoidModel<S>, A extends HumanoidModel<S>> boolean tryRenderGeoRenderLayer(
            PoseStack poseStack, MultiBufferSource bufferSource, S humanoidRenderState, ItemStack stack, EquipmentSlot equipmentSlot,
            M parentModel, A baseModel, int packedLight, float netHeadYaw, float headPitch, BiConsumer<A, EquipmentSlot> partVisibilitySetter) {
        GeoRenderProvider renderProvider;

        if (!HumanoidArmorLayer.shouldRender(stack, equipmentSlot) || (renderProvider = GeoRenderProvider.of(stack)) == GeoRenderProvider.DEFAULT || !(humanoidRenderState instanceof GeoRenderState geoRenderState))
            return false;

        final GeoArmorRenderer armorRenderer = renderProvider.getGeoArmorRenderer(humanoidRenderState, stack, equipmentSlot,
                equipmentSlot == EquipmentSlot.LEGS ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID, baseModel);
        if (armorRenderer == null)
            return false;

        Map<DataTicket<?>, Object> existingRenderData = null;

        EnumMap<EquipmentSlot, Map<DataTicket<?>, Object>> perSlotData = geoRenderState.hasGeckolibData(DataTickets.PER_SLOT_RENDER_DATA) ? geoRenderState.getGeckolibData(DataTickets.PER_SLOT_RENDER_DATA) : null;

        geoRenderState.addGeckolibData(DataTickets.PACKED_LIGHT, packedLight);

        if (perSlotData != null && perSlotData.containsKey(equipmentSlot)) {
            Map<DataTicket<?>, Object> renderData = geoRenderState.getDataMap();
            existingRenderData = new Reference2ObjectOpenHashMap<>(renderData);
            renderData.clear();
            renderData.putAll(perSlotData.get(equipmentSlot));
        }

        geoRenderState.addGeckolibData(DataTickets.HUMANOID_MODEL, baseModel);
        geoRenderState.addGeckolibData(DataTickets.PACKED_LIGHT, packedLight);

        parentModel.copyPropertiesTo(baseModel);
        partVisibilitySetter.accept(baseModel, equipmentSlot);
        baseModel.copyPropertiesTo(armorRenderer);

        RenderType renderType = armorRenderer.getRenderType(humanoidRenderState, armorRenderer.getTextureLocation(geoRenderState));
        VertexConsumer buffer = renderType == null ? null : ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, geoRenderState.getGeckolibData(DataTickets.HAS_GLINT));

        armorRenderer.defaultRender(geoRenderState, poseStack, bufferSource, renderType, buffer);

        if (existingRenderData != null) {
            geoRenderState.getDataMap().clear();
            geoRenderState.getDataMap().putAll(existingRenderData);
        }

        return true;
    }
}
