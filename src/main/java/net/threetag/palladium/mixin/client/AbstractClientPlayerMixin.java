package net.threetag.palladium.mixin.client;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import net.threetag.palladium.client.renderer.entity.skin.PlayerSkinHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        var originalSkin = cir.getReturnValue();
        cir.setReturnValue(PlayerSkinHandler.getCurrentSkin((AbstractClientPlayer) (Object) this, originalSkin));
    }
}
