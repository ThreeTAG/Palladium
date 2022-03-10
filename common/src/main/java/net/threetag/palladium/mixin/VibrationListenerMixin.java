package net.threetag.palladium.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationListener;
import net.threetag.palladium.tags.PalladiumItemTags;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationListener.class)
public class VibrationListenerMixin {

    @Inject(at = @At("HEAD"), method = "isValidVibration", cancellable = true)
    private void isValidVibration(GameEvent gameEvent, @Nullable Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.FEET).is(PalladiumItemTags.VIBRATION_ABSORPTION_BOOTS)) {
            if (gameEvent == GameEvent.HIT_GROUND || gameEvent == GameEvent.STEP || gameEvent == GameEvent.ELYTRA_FREE_FALL) {
                info.setReturnValue(false);
            }
        }
    }

}
