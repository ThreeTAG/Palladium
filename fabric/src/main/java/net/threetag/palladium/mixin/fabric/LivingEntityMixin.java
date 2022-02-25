package net.threetag.palladium.mixin.fabric;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.event.PalladiumEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        PalladiumEvents.LIVING_UPDATE.invoker().tick(entity);
    }

}
