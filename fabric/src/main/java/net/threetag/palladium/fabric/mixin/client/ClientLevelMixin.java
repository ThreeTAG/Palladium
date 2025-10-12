package net.threetag.palladium.fabric.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.core.event.PalladiumEntityEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V"), method = "tickNonPassenger")
    private void tickPre(Entity entity, CallbackInfo ci) {
        PalladiumEntityEvents.TICK_PRE.invoker().entityTick(entity);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;tick()V", shift = At.Shift.AFTER), method = "tickNonPassenger")
    private void tickPost(Entity entity, CallbackInfo ci) {
        PalladiumEntityEvents.TICK_POST.invoker().entityTick(entity);
    }

}
