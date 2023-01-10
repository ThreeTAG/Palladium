package net.threetag.palladium.mixin.client;

import net.minecraft.client.resources.SplashManager;
import net.threetag.palladium.util.SplashTextUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SplashManager.class)
public class SplashManagerMixin {

    @Inject(method = "getSplash", at = @At("HEAD"), cancellable = true)
    public void getSplash(CallbackInfoReturnable<String> ci) {
        var splash = SplashTextUtil.getPossibleOverrideSplash();

        if(splash != null) {
            ci.setReturnValue(splash);
        }
    }
}
