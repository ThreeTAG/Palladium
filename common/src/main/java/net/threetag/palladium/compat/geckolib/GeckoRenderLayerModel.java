package net.threetag.palladium.compat.geckolib;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.controller.AnimationController;
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

public class GeckoRenderLayerModel extends HumanoidModel<AbstractClientPlayer> implements IGeoRenderer<GeckoRenderLayer>, AnimationController.ModelFetcher<GeckoRenderLayer> {

    protected MultiBufferSource rtb;
    private final GeckoRenderLayer renderLayer;
    protected final AnimatedGeoModel<GeckoRenderLayer> modelProvider;

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

    @Override
    public MultiBufferSource getCurrentRTB() {
        return this.rtb;
    }

    public void setCurrentRTB(MultiBufferSource rtb) {
        this.rtb = rtb;
    }

    public void renderModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelResource(this.renderLayer));
        var partialTick = Minecraft.getInstance().getFrameTime();
        AnimationEvent<GeckoRenderLayer> animationEvent = new AnimationEvent<>(this.renderLayer, 0, 0,
                partialTick, false,
                List.of());

        poseStack.pushPose();
        poseStack.translate(0, 24 / 16F, 0);
        poseStack.scale(-1, -1, 1);

        this.modelProvider.setCustomAnimations(this.renderLayer, getInstanceId(this.renderLayer), animationEvent); // TODO change to setCustomAnimations in 1.20+
        setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
        fitToBiped();
        RenderSystem.setShaderTexture(0, getTextureLocation(this.renderLayer));

        var buffer = this.renderLayer.renderType.apply(bufferSource, getTextureLocation(this.renderLayer));
        Color renderColor = getRenderColor(this.renderLayer, partialTick, poseStack, null, buffer, packedLight);

        render(model, this.renderLayer, partialTick, null, poseStack, null, buffer, packedLight,
                packedOverlay, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);

        poseStack.popPose();
    }

    public void renderArm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay,
                          float partialTick, boolean rightArm) {
        GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelResource(this.renderLayer));

        model.getBone(rightArm ? this.rightArmBone : this.leftArmBone).ifPresent(bone -> {
            AnimationEvent<GeckoRenderLayer> animationEvent = new AnimationEvent<>(this.renderLayer, 0, 0,
                    partialTick, false,
                    List.of());

            poseStack.pushPose();
            poseStack.translate(0, 24 / 16F, 0);
            poseStack.scale(-1, -1, 1);

            this.modelProvider.setCustomAnimations(this.renderLayer, getInstanceId(this.renderLayer), animationEvent); // TODO change to setCustomAnimations in 1.20+
            setCurrentModelRenderCycle(EModelRenderCycle.INITIAL);
            fitToBiped();
            RenderSystem.setShaderTexture(0, getTextureLocation(this.renderLayer));

            var buffer1 = this.renderLayer.renderType.apply(bufferSource, getTextureLocation(this.renderLayer));
            Color renderColor = getRenderColor(this.renderLayer, 0, poseStack, null, buffer1, packedLight);

            setCurrentRTB(bufferSource);
            renderEarly(this.renderLayer, poseStack, partialTick, bufferSource, buffer1, packedLight,
                    packedOverlay, renderColor.getRed() / 255f, renderColor.getGreen() / 255f,
                    renderColor.getBlue() / 255f, renderColor.getAlpha() / 255f);

            renderLate(this.renderLayer, poseStack, partialTick, bufferSource, buffer1, packedLight,
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
            IBone headBone = this.modelProvider.getBone(this.headBone);

            GeoUtils.copyRotations(this.head, headBone);
            headBone.setPositionX(this.head.x);
            headBone.setPositionY(-this.head.y);
            headBone.setPositionZ(this.head.z);
            headBone.setHidden(!this.head.visible);
        }

        if (this.bodyBone != null) {
            IBone bodyBone = this.modelProvider.getBone(this.bodyBone);

            GeoUtils.copyRotations(this.body, bodyBone);
            bodyBone.setPositionX(this.body.x);
            bodyBone.setPositionY(-this.body.y);
            bodyBone.setPositionZ(this.body.z);
            bodyBone.setHidden(!this.body.visible);
        }

        if (this.rightArmBone != null) {
            IBone rightArmBone = this.modelProvider.getBone(this.rightArmBone);

            GeoUtils.copyRotations(this.rightArm, rightArmBone);
            rightArmBone.setPositionX(this.rightArm.x + 5);
            rightArmBone.setPositionY(2 - this.rightArm.y);
            rightArmBone.setPositionZ(this.rightArm.z);
            rightArmBone.setHidden(!this.rightArm.visible);
        }

        if (this.leftArmBone != null) {
            IBone leftArmBone = this.modelProvider.getBone(this.leftArmBone);

            GeoUtils.copyRotations(this.leftArm, leftArmBone);
            leftArmBone.setPositionX(this.leftArm.x - 5);
            leftArmBone.setPositionY(2 - this.leftArm.y);
            leftArmBone.setPositionZ(this.leftArm.z);
            leftArmBone.setHidden(!this.leftArm.visible);
        }

        if (this.rightLegBone != null) {
            IBone rightLegBone = this.modelProvider.getBone(this.rightLegBone);

            GeoUtils.copyRotations(this.rightLeg, rightLegBone);
            rightLegBone.setPositionX(this.rightLeg.x + 2);
            rightLegBone.setPositionY(12 - this.rightLeg.y);
            rightLegBone.setPositionZ(this.rightLeg.z);
            rightLegBone.setHidden(!this.rightLeg.visible);
        }

        if (this.leftLegBone != null) {
            IBone leftLegBone = this.modelProvider.getBone(this.leftLegBone);

            GeoUtils.copyRotations(this.leftLeg, leftLegBone);
            leftLegBone.setPositionX(this.leftLeg.x - 2);
            leftLegBone.setPositionY(12 - this.leftLeg.y);
            leftLegBone.setPositionZ(this.leftLeg.z);
            leftLegBone.setHidden(!this.leftLeg.visible);
        }
    }

    @Override
    public GeoModelProvider<GeckoRenderLayer> getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(GeckoRenderLayer animatable) {
        return this.modelProvider.getTextureResource(this.renderLayer);
    }

    @Override
    public IAnimatableModel<GeckoRenderLayer> apply(IAnimatable animatable) {
        return animatable instanceof GeckoRenderLayer layer ? layer.getGeoModel() : null;
    }

    @Override
    public int getInstanceId(GeckoRenderLayer animatable) {
        return Objects.hash(animatable);
    }
}
