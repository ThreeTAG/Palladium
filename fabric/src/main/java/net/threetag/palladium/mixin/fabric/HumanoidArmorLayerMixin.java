package net.threetag.palladium.mixin.fabric;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.model.ArmorModelManager;
import net.threetag.palladium.item.ExtendedArmor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    @Unique
    private LivingEntity cachedEntity;

    @Unique
    private ItemStack cachedStack;

    @Unique
    private EquipmentSlot cachedSlot;

    @Shadow
    protected abstract void setPartVisibility(A model, EquipmentSlot slot);

    @Shadow
    protected abstract boolean usesInnerModel(EquipmentSlot slot);

    @Shadow
    public abstract void render(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);

    @Shadow
    protected abstract void renderModel(PoseStack poseStack, MultiBufferSource buffer, int i, ArmorItem armorItem, boolean bl, A model, boolean bl2, float f, float g, float h, @Nullable String string);

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(at = @At("HEAD"), method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V")
    public void renderHook(PoseStack matrixStack, MultiBufferSource buffer, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        this.cachedEntity = livingEntity;
    }

    @Inject(at = @At("HEAD"), method = "getArmorLocation", cancellable = true)
    private void getArmorLocation(ArmorItem armorItem, boolean bl, @Nullable String string, CallbackInfoReturnable<ResourceLocation> cir) {
        var texture = getTexture(this.cachedStack, this.cachedEntity, this.cachedSlot, string, ArmorModelManager.get(armorItem));

        if (texture != null) {
            cir.setReturnValue(texture);
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(at = @At("HEAD"), method = "renderArmorPiece", cancellable = true)
    private void renderArmorPiece(PoseStack poseStack, MultiBufferSource multiBufferSource, T livingEntity, EquipmentSlot equipmentSlot, int i, A humanoidModel, CallbackInfo callbackInfo) {
        ItemStack itemStack = livingEntity.getItemBySlot(equipmentSlot);
        this.cachedStack = itemStack;

        if (!itemStack.isEmpty()) {
            ArmorItem armorItem = (ArmorItem) itemStack.getItem();
            ArmorModelManager.Handler handler = ArmorModelManager.get(itemStack.getItem());

            if (handler != null) {
                HumanoidModel<?> model = handler.getArmorModel(itemStack, livingEntity, equipmentSlot);

                if (model != null) {
                    humanoidModel = (A) model;
                }

                if (armorItem.getSlot() == equipmentSlot) {
                    this.getParentModel().copyPropertiesTo(humanoidModel);
                    this.setPartVisibility(humanoidModel, equipmentSlot);
                    boolean bl = this.usesInnerModel(equipmentSlot);
                    boolean bl2 = itemStack.hasFoil();
                    if (armorItem instanceof DyeableArmorItem) {
                        int j = ((DyeableArmorItem) armorItem).getColor(itemStack);
                        float f = (float) (j >> 16 & 255) / 255.0F;
                        float g = (float) (j >> 8 & 255) / 255.0F;
                        float h = (float) (j & 255) / 255.0F;
                        this.renderModel(poseStack, multiBufferSource, i, armorItem, bl2, humanoidModel, bl, f, g, h, null);
                        this.renderModel(poseStack, multiBufferSource, i, armorItem, bl2, humanoidModel, bl, 1.0F, 1.0F, 1.0F, "overlay");
                    } else {
                        this.renderModel(poseStack, multiBufferSource, i, armorItem, bl2, humanoidModel, bl, 1.0F, 1.0F, 1.0F, null);
                    }
                }
                callbackInfo.cancel();
            }
        }
    }

    private ResourceLocation getTexture(ItemStack stack, LivingEntity entity, EquipmentSlot slot, String type, @Nullable ArmorModelManager.Handler handler) {
        if (handler != null) {
            var texture = handler.getTexture(stack, entity, slot, type);

            if (texture != null) {
                return texture;
            }
        }

        if (stack.getItem() instanceof ExtendedArmor extendedArmor) {
            return extendedArmor.getArmorTextureLocation(stack, entity, slot, type);
        }

        return null;
    }

}
