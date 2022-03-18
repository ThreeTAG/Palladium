package net.threetag.palladium.mixin.forge;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.client.ArmorModelManager;
import net.threetag.palladium.item.ICustomArmorTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(HumanoidArmorLayer.class)
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> extends RenderLayer<T, M> {

    @Shadow
    protected abstract void setPartVisibility(A model, EquipmentSlot slot);

    public HumanoidArmorLayerMixin(RenderLayerParent<T, M> renderLayerParent) {
        super(renderLayerParent);
    }

    @Inject(at = @At("HEAD"), method = "getArmorResource", cancellable = true, remap = false)
    private void getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type, CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (stack.getItem() instanceof ICustomArmorTexture customArmorTexture) {
            callbackInfoReturnable.setReturnValue(customArmorTexture.getArmorTexture(stack, entity, slot, type));
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
}
