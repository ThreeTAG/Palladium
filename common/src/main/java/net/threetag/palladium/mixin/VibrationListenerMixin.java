package net.threetag.palladium.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.tags.PalladiumItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationSystem.Listener.class)
public class VibrationListenerMixin {

    @Inject(at = @At("HEAD"), method = "handleGameEvent", cancellable = true)
    public void handleGameEvent(ServerLevel level, GameEvent gameEvent, GameEvent.Context context, Vec3 pos, CallbackInfoReturnable<Boolean> info) {
        if (context.sourceEntity() instanceof LivingEntity livingEntity && livingEntity.getItemBySlot(EquipmentSlot.FEET).is(PalladiumItemTags.VIBRATION_ABSORPTION_BOOTS)) {
            if (gameEvent == GameEvent.HIT_GROUND || gameEvent == GameEvent.STEP) {
                info.setReturnValue(false);
            }
        }
    }

}
