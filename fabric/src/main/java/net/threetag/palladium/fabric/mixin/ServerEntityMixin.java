package net.threetag.palladium.fabric.mixin;

import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.core.event.PalladiumPlayerEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerEntity.class)
public class ServerEntityMixin {

    @Shadow
    @Final
    private Entity entity;

    @Inject(at = @At("RETURN"), method = "addPairing")
    private void addPairing(ServerPlayer pPlayer, CallbackInfo ci) {
        PalladiumPlayerEvents.START_TRACKING.invoker().playerTracking(pPlayer, this.entity);
    }

    @Inject(at = @At("RETURN"), method = "removePairing")
    private void removePairing(ServerPlayer pPlayer, CallbackInfo ci) {
        PalladiumPlayerEvents.STOP_TRACKING.invoker().playerTracking(pPlayer, this.entity);
    }

}