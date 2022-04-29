package net.threetag.palladium.mixin.fabric;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.threetag.palladium.entity.fabric.EntityAttributeModificationRegistryImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultAttributes.class)
public class DefaultAttributesMixin {

    @Inject(at = @At("HEAD"), method = "getSupplier", cancellable = true)
    private static void getSupplier(EntityType<? extends LivingEntity> livingEntity, CallbackInfoReturnable<AttributeSupplier> ci) {
        AttributeSupplier supplier = EntityAttributeModificationRegistryImpl.getAttributesView().get(livingEntity);

        if (supplier != null) {
            ci.setReturnValue(supplier);
        }
    }

    @Inject(at = @At("HEAD"), method = "hasSupplier", cancellable = true)
    private static void hasSupplier(EntityType<?> entityType, CallbackInfoReturnable<Boolean> ci) {
        AttributeSupplier supplier = EntityAttributeModificationRegistryImpl.getAttributesView().get(entityType);

        if (supplier != null) {
            ci.setReturnValue(true);
        }
    }

}
