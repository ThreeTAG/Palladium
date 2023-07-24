package net.threetag.palladium.mixin.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.model.ArmorModelManager;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.client.renderer.entity.HumanoidRendererModifications;
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
    private ItemStack palladium$cachedItem;

    @Shadow
    protected abstract void setPartVisibility(A model, EquipmentSlot slot);

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(at = @At("HEAD"), method = "getArmorResource", cancellable = true, remap = false)
    private void getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type, CallbackInfoReturnable<ResourceLocation> cir) {
        ArmorModelManager.Handler handler = ArmorModelManager.get(stack.getItem());
        this.palladium$cachedItem = stack;

        if (handler != null && entity instanceof LivingEntity livingEntity) {
            var texture = handler.getTexture(stack, livingEntity, slot, type);

            if (texture != null) {
                cir.setReturnValue(texture);
                return;
            }
        }

        if (stack.getItem() instanceof ExtendedArmor extendedArmor) {
            var texture = extendedArmor.getArmorTextureLocation(stack, entity, slot, type);

            if (texture != null) {
                cir.setReturnValue(texture);
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Inject(at = @At("HEAD"), method = "getArmorModelHook", cancellable = true, remap = false)
    private void getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel<?> model, CallbackInfoReturnable<Model> ci) {
        ArmorModelManager.Handler handler = ArmorModelManager.get(itemStack.getItem());

        if (handler != null) {
            HumanoidModel m = handler.getArmorModel(itemStack, entity, slot);

            if (model != null) {
                model.copyPropertiesTo(m);
                this.setPartVisibility((A) m, slot);
                ci.setReturnValue(m);
            }
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/layers/HumanoidArmorLayer;setPartVisibility(Lnet/minecraft/client/model/HumanoidModel;Lnet/minecraft/world/entity/EquipmentSlot;)V", shift = At.Shift.AFTER),
            method = "renderArmorPiece")
    private void renderArmorPieceCopyModelProperties(PoseStack poseStack, MultiBufferSource buffer, T livingEntity, EquipmentSlot slot, int i, A model, CallbackInfo ci) {
        HumanoidRendererModifications.applyRemovedBodyParts(model);
    }

    @Inject(method = "renderModel(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IZLnet/minecraft/client/model/Model;FFFLnet/minecraft/resources/ResourceLocation;)V", at = @At("HEAD"), cancellable = true)
    private void renderModel(PoseStack arg, MultiBufferSource arg2, int i, boolean bl, Model arg3, float f, float g, float h, ResourceLocation armorResource, CallbackInfo ci) {
        if (this.palladium$cachedItem.getItem() instanceof ExtendedArmor) {
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(arg2, PalladiumRenderTypes.ARMOR_CUTOUT_NO_CULL_TRANSPARENCY.apply(armorResource), false, bl);
            arg3.renderToBuffer(arg, vertexconsumer, i, OverlayTexture.NO_OVERLAY, f, g, h, 1.0F);
            ci.cancel();
        }
    }
}
