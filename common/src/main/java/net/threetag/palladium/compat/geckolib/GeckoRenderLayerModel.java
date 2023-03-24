package net.threetag.palladium.compat.geckolib;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;
import software.bernie.geckolib3.util.GeoUtils;

import java.util.List;
import java.util.Objects;

public class GeckoRenderLayerModel extends HumanoidModel<AbstractClientPlayer> implements IGeoRenderer<GeckoLayerState> {

    protected MultiBufferSource rtb;
    private final GeckoRenderLayer renderLayer;
    private GeckoLayerState state;
    protected final AnimatedGeoModel<GeckoLayerState> modelProvider;
    public LivingEntity entityLiving;

    public String headBone = "armorHead";
    public String bodyBone = "armorBody";
    public String rightArmBone = "armorRightArm";
    public String leftArmBone = "armorLeftArm";
    public String rightLegBone = "armorRightLeg";
    public String leftLegBone = "armorLeftLeg";

    public GeckoRenderLayerModel(GeckoRenderLayer renderLayer) {
        super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        this.renderLayer = renderLayer;
        this.modelProvider = renderLayer.getGeoModel();
    }

    public void setRenderedEntity(LivingEntity entityLiving) {
        this.entityLiving = entityLiving;
        this.state = this.renderLayer.getState(entityLiving);
    }

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    public void setCurrentRTB(MultiBufferSource rtb) {
        this.rtb = rtb;
    }

    public void renderModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelResource(this.state));
        var partialTick = Minecraft.getInstance().getFrameTime();
        AnimationEvent<GeckoLayerState> animationEvent = new AnimationEvent<>(this.state, 0, 0,
                partialTick, false,
                List.of());

        poseStack.pushPose();
        poseStack.translate(0, 24 / 16F, 0);
        poseStack.scale(-1, -1, 1);

        this.modelProvider.setCustomAnimations(this.state, getInstanceId(this.state), animationEvent);
        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        fitToBiped();
        RenderSystem.setShaderTexture(0, getTextureLocation(this.state));

        var buffer = this.renderLayer.renderType.apply(bufferSource, getTextureLocation(this.state));
        Color renderColor = getRenderColor(this.state, partialTick, poseStack, null, buffer, packedLight);

        render(model, this.state, partialTick, null, poseStack, null, buffer, packedLight,
                packedOverlay, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);

        poseStack.popPose();
    }

    public void renderArm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay,
                          float partialTick, boolean rightArm) {
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelResource(this.state));

        model.getBone(rightArm ? this.rightArmBone : this.leftArmBone).ifPresent(bone -> {
            AnimationEvent<GeckoLayerState> animationEvent = new AnimationEvent<>(this.state, 0, 0,
                    partialTick, false,
                    List.of());

            poseStack.pushPose();
            poseStack.translate(0, 24 / 16F, 0);
            poseStack.scale(-1, -1, 1);

            this.modelProvider.setCustomAnimations(this.state, getInstanceId(this.state), animationEvent);
            setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
            fitToBiped();
            RenderSystem.setShaderTexture(0, getTextureLocation(this.state));

            var buffer1 = this.renderLayer.renderType.apply(bufferSource, getTextureLocation(this.state));
            Color renderColor = getRenderColor(this.state, 0, poseStack, null, buffer1, packedLight);

            setCurrentRTB(bufferSource);
            renderEarly(this.state, poseStack, partialTick, bufferSource, buffer1, packedLight,
                    packedOverlay, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                    renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);

            renderLate(this.state, poseStack, partialTick, bufferSource, buffer1, packedLight,
                    packedOverlay, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                    renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
            this.renderRecursively(bone,
                    poseStack, buffer1, packedLight, packedOverlay, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                    renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);
            setCurrentModelRenderCycle(EModelRenderCycle.REPEATED);

            poseStack.popPose();
        });
    }

    protected void fitToBiped() {
        if (this.headBone != null) {
            try {
                IBone headBone = this.modelProvider.getBone(this.headBone);

                GeoUtils.copyRotations(this.head, headBone);
                copyScale(this.head, headBone);
                headBone.setPositionX(this.head.x);
                headBone.setPositionY(-this.head.y);
                headBone.setPositionZ(this.head.z);
                headBone.setHidden(!this.head.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.bodyBone != null) {
            try {
                IBone bodyBone = this.modelProvider.getBone(this.bodyBone);

                GeoUtils.copyRotations(this.body, bodyBone);
                copyScale(this.body, bodyBone);
                bodyBone.setPositionX(this.body.x);
                bodyBone.setPositionY(-this.body.y);
                bodyBone.setPositionZ(this.body.z);
                bodyBone.setHidden(!this.body.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.rightArmBone != null) {
            try {
                IBone rightArmBone = this.modelProvider.getBone(this.rightArmBone);

                GeoUtils.copyRotations(this.rightArm, rightArmBone);
                copyScale(this.rightArm, rightArmBone);
                rightArmBone.setPositionX(this.rightArm.x + 5);
                rightArmBone.setPositionY(2 - this.rightArm.y);
                rightArmBone.setPositionZ(this.rightArm.z);
                rightArmBone.setHidden(!this.rightArm.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.leftArmBone != null) {
            try {
                IBone leftArmBone = this.modelProvider.getBone(this.leftArmBone);

                GeoUtils.copyRotations(this.leftArm, leftArmBone);
                copyScale(this.leftArm, leftArmBone);
                leftArmBone.setPositionX(this.leftArm.x - 5);
                leftArmBone.setPositionY(2 - this.leftArm.y);
                leftArmBone.setPositionZ(this.leftArm.z);
                leftArmBone.setHidden(!this.leftArm.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.rightLegBone != null) {
            try {
                IBone rightLegBone = this.modelProvider.getBone(this.rightLegBone);

                GeoUtils.copyRotations(this.rightLeg, rightLegBone);
                copyScale(this.rightLeg, rightLegBone);
                rightLegBone.setPositionX(this.rightLeg.x + 2);
                rightLegBone.setPositionY(12 - this.rightLeg.y);
                rightLegBone.setPositionZ(this.rightLeg.z);
                rightLegBone.setHidden(!this.rightLeg.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.leftLegBone != null) {
            try {
                IBone leftLegBone = this.modelProvider.getBone(this.leftLegBone);

                GeoUtils.copyRotations(this.leftLeg, leftLegBone);
                copyScale(this.leftLeg, leftLegBone);
                leftLegBone.setPositionX(this.leftLeg.x - 2);
                leftLegBone.setPositionY(12 - this.leftLeg.y);
                leftLegBone.setPositionZ(this.leftLeg.z);
                leftLegBone.setHidden(!this.leftLeg.visible);
            } catch (Exception ignored) {

            }
        }
    }

    public static void copyScale(ModelPart part, IBone bone) {
        bone.setScaleX(part.xScale);
        bone.setScaleY(part.yScale);
        bone.setScaleZ(part.zScale);
    }

    @Override
    public GeoModelProvider<GeckoLayerState> getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(GeckoLayerState animatable) {
        return this.modelProvider.getTextureResource(this.state);
    }

    @Override
    public int getInstanceId(GeckoLayerState animatable) {
        return Objects.hash(animatable);
    }
}
