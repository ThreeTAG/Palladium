package net.threetag.palladium.fabric.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("RETURN"), method = "getVisibilityPercent", cancellable = true)
    private void getVisibilityPercent(@Nullable Entity pLookingEntity, CallbackInfoReturnable<Double> ci) {
        AbilityUtil.getEnabledInstances((LivingEntity) (Object) this, AbilitySerializers.INVISIBILITY.get()).forEach(instance -> {
            ci.setReturnValue(ci.getReturnValueD() * instance.getAbility().mobVisibilityModifier);
        });
    }

}
