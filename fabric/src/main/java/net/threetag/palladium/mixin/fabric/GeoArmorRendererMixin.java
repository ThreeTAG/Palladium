package net.threetag.palladium.mixin.fabric;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.compat.geckolib.armor.GeckoArmorRenderer;
import net.threetag.palladium.compat.geckolib.renderlayer.GeckoRenderLayerModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
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

    @Shadow
    protected HumanoidModel baseModel;

    @Inject(method = "fitToBiped", at = @At("RETURN"), remap = false)
    private void fitToBiped(CallbackInfo ci) {

        if (this.headBone != null) {
            try {
                IBone headBone = this.modelProvider.getBone(this.headBone);

                GeoUtils.copyRotations(this.baseModel.head, headBone);
                GeckoRenderLayerModel.copyScale(this.baseModel.head, headBone);
                headBone.setHidden(!this.baseModel.head.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.bodyBone != null) {
            try {
                IBone bodyBone = this.modelProvider.getBone(this.bodyBone);

                GeckoRenderLayerModel.copyScale(this.baseModel.body, bodyBone);
                bodyBone.setHidden(!this.baseModel.body.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.rightArmBone != null) {
            try {
                IBone rightArmBone = this.modelProvider.getBone(this.rightArmBone);

                GeckoRenderLayerModel.copyScale(this.baseModel.rightArm, rightArmBone);
                rightArmBone.setHidden(!this.baseModel.rightArm.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.leftArmBone != null) {
            try {
                IBone leftArmBone = this.modelProvider.getBone(this.leftArmBone);

                GeckoRenderLayerModel.copyScale(this.baseModel.leftArm, leftArmBone);
                leftArmBone.setHidden(!this.baseModel.leftArm.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.rightLegBone != null) {
            try {
                IBone rightLegBone = this.modelProvider.getBone(this.rightLegBone);

                GeckoRenderLayerModel.copyScale(this.baseModel.rightLeg, rightLegBone);
                rightLegBone.setHidden(!this.baseModel.rightLeg.visible);
            } catch (Exception ignored) {

            }
        }

        if (this.leftLegBone != null) {
            try {
                IBone leftLegBone = this.modelProvider.getBone(this.leftLegBone);

                GeckoRenderLayerModel.copyScale(this.baseModel.leftLeg, leftLegBone);
                leftLegBone.setHidden(!this.baseModel.leftLeg.visible);
            } catch (Exception ignored) {

            }
        }
    }

    @Inject(method = "setCurrentItem", at = @At("HEAD"), remap = false)
    public void setCurrentItem(LivingEntity entity, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel model, CallbackInfoReturnable<GeoArmorRenderer> cir) {
        GeckoArmorRenderer.RENDERED_ENTITY = entity;
    }

}
