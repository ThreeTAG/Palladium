package net.threetag.palladium.mixin.forge;

import net.minecraft.client.model.HumanoidModel;
import net.threetag.palladium.compat.geckolib.renderlayer.GeckoRenderLayerModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import software.bernie.geckolib3.util.GeoUtils;

@SuppressWarnings("rawtypes")
@Mixin(GeoArmorRenderer.class)
public class GeoArmorRendererMixin {

    @Shadow
    public String headBone;

    @Shadow
    @Final
    private AnimatedGeoModel modelProvider;

    @Shadow
    public String leftLegBone;

    @Shadow
    public String rightLegBone;

    @Shadow
    public String leftArmBone;

    @Shadow
    public String rightArmBone;

    @Shadow
    public String bodyBone;

    @Inject(method = "fitToBiped", at = @At("RETURN"), remap = false)
    private void fitToBiped(CallbackInfo ci) {
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;

        if (this.headBone != null) {
            try {
                IBone headBone = this.modelProvider.getBone(this.headBone);

                GeoUtils.copyRotations(model.head, headBone);
                GeckoRenderLayerModel.copyScale(model.head, headBone);
                headBone.setHidden(!model.head.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.bodyBone != null) {
            try {
                IBone bodyBone = this.modelProvider.getBone(this.bodyBone);

                GeckoRenderLayerModel.copyScale(model.body, bodyBone);
                bodyBone.setHidden(!model.body.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.rightArmBone != null) {
            try {
                IBone rightArmBone = this.modelProvider.getBone(this.rightArmBone);

                GeckoRenderLayerModel.copyScale(model.rightArm, rightArmBone);
                rightArmBone.setHidden(!model.rightArm.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.leftArmBone != null) {
            try {
                IBone leftArmBone = this.modelProvider.getBone(this.leftArmBone);

                GeckoRenderLayerModel.copyScale(model.leftArm, leftArmBone);
                leftArmBone.setHidden(!model.leftArm.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.rightLegBone != null) {
            try {
                IBone rightLegBone = this.modelProvider.getBone(this.rightLegBone);

                GeckoRenderLayerModel.copyScale(model.rightLeg, rightLegBone);
                rightLegBone.setHidden(!model.rightLeg.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.leftLegBone != null) {
            try {
                IBone leftLegBone = this.modelProvider.getBone(this.leftLegBone);

                GeckoRenderLayerModel.copyScale(model.leftLeg, leftLegBone);
                leftLegBone.setHidden(!model.leftLeg.visible);
            } catch (Exception ignored) {

            }
        }
    }

}
