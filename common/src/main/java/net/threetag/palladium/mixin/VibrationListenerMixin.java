package net.threetag.palladium.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.threetag.palladium.tags.PalladiumItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationListener.class)
public class VibrationListenerMixin {

    @Inject(at = @At("HEAD"), method = "handleGameEvent", cancellable = true)
    private void isValidVibration(ServerLevel serverLevel, GameEvent.Message message, CallbackInfoReturnable<Boolean> info) {
        if (message.context().sourceEntity() instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.FEET).is(PalladiumItemTags.VIBRATION_ABSORPTION_BOOTS)) {
            if (message.gameEvent() == GameEvent.HIT_GROUND || message.gameEvent() == GameEvent.STEP) {
                info.setReturnValue(false);
            }
        }
    }

}
