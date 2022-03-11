package net.threetag.palladium.mixin.forge;

import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.item.ICustomArmorTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {

    @Inject(at = @At("HEAD"), method = "getArmorResource", cancellable = true, remap = false)
    private void getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type, CallbackInfoReturnable<ResourceLocation> callbackInfoReturnable) {
        if (stack.getItem() instanceof ICustomArmorTexture customArmorTexture) {
            callbackInfoReturnable.setReturnValue(customArmorTexture.getArmorTexture(stack, entity, slot, type));
        }
    }
}
