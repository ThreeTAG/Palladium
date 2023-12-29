package net.threetag.palladium.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.client.renderer.item.armor.ArmorRendererData;
import net.threetag.palladium.compat.geckolib.armor.CancelGeckoArmorBuffer;
import net.threetag.palladium.item.ArmorWithRenderer;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"unchecked", "rawtypes", "DataFlowIssue"})
@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin {

    @Shadow
    protected abstract void setPartVisibility(HumanoidModel model, EquipmentSlot slot);

    @Shadow
    protected abstract boolean usesInnerModel(EquipmentSlot slot);

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, LivingEntity livingEntity, EquipmentSlot slot, int packedLight, HumanoidModel model, CallbackInfo ci) {
        var item = livingEntity.getItemBySlot(slot);

        if (!item.isEmpty() && item.getItem() instanceof ArmorWithRenderer armorWithRenderer && armorWithRenderer.hasCustomRenderer()) {
            ci.cancel();

            if (armorWithRenderer.getCachedArmorRenderer() instanceof ArmorRendererData renderer) {
                HumanoidArmorLayer layer = (HumanoidArmorLayer) (Object) this;
                ArmorItem armorItem = (ArmorItem) item.getItem();

                if (armorItem.getEquipmentSlot() == slot) {
                    var context = DataContext.forArmorInSlot(livingEntity, slot);
                    var overriddenModel = renderer.getModel(livingEntity, context);
                    var armorTexture = renderer.getTexture(context);

                    if (overriddenModel != null) {
                        model = overriddenModel;
                    }

                    ((HumanoidModel) layer.getParentModel()).copyPropertiesTo(model);
                    this.setPartVisibility(model, slot);
                    boolean innerModel = this.usesInnerModel(slot);
                    boolean foil = item.hasFoil();
                    if (armorItem instanceof DyeableArmorItem) {
                        var overlayTexture = renderer.getTexture(context, "overlay");
                        int j = ((DyeableArmorItem) armorItem).getColor(item);
                        float f = (float) (j >> 16 & 255) / 255.0F;
                        float g = (float) (j >> 8 & 255) / 255.0F;
                        float h = (float) (j & 255) / 255.0F;
                        this.palladium$renderModelCustom(poseStack, buffer, packedLight, foil, model, armorTexture, innerModel, f, g, h);
                        this.palladium$renderModelCustom(poseStack, buffer, packedLight, foil, model, overlayTexture, innerModel, 1.0F, 1.0F, 1.0F);
                    } else {
                        this.palladium$renderModelCustom(poseStack, buffer, packedLight, foil, model, armorTexture, innerModel, 1.0F, 1.0F, 1.0F);
                    }

//                    HumanoidModel finalModel = model;
//                    ArmorTrim.getTrim(livingEntity.level().registryAccess(), item).ifPresent((armorTrim) -> {
//                        this.renderTrim(armorItem.getMaterial(), poseStack, buffer, packedLight, armorTrim, finalModel, innerModel);
//                    });
//                    if (item.hasFoil()) {
//                        this.renderGlint(poseStack, buffer, packedLight, finalModel);
//                    }
                }
            }

            ci.cancel();
        }
    }

    @Unique
    private void palladium$renderModelCustom(PoseStack poseStack, MultiBufferSource buffer, int packedLight, boolean foil, HumanoidModel model, ResourceLocation texture, boolean innerModel, float red, float green, float blue) {
        VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, PalladiumRenderTypes.getArmorTranslucent(texture), false, foil);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    private void renderModel(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ArmorItem armorItem, HumanoidModel model, boolean withGlint, float red, float green, float blue, @Nullable String armorSuffix, CallbackInfo ci) {
        if (model instanceof CancelGeckoArmorBuffer) {
            model.renderToBuffer(poseStack, null, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
            ci.cancel();
        }
    }

}
