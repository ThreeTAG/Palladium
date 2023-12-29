package net.threetag.palladium.mixin.fabric;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "getVisibilityPercent", cancellable = true)
    private void getVisibilityPercent(@Nullable Entity pLookingEntity, CallbackInfoReturnable<Double> ci) {
        if (!AbilityUtil.getEnabledEntries((LivingEntity) (Object) this, Abilities.INVISIBILITY.get()).isEmpty()) {
            ci.setReturnValue(0D);
        }
    }

}
