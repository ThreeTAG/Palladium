package net.threetag.palladium.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.RestrictSlotsAbility;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "canUseSlot", at = @At("HEAD"), cancellable = true)
    public void canUseSlot(EquipmentSlot slot, CallbackInfoReturnable<Boolean> cir) {
        if (RestrictSlotsAbility.isRestricted((LivingEntity) (Object) this, slot)) {
            cir.setReturnValue(false);
        }
    }

}
