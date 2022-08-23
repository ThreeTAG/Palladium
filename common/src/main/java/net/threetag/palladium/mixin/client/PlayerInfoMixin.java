package net.threetag.palladium.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.renderer.entity.PlayerSkinHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {

    @Shadow @Final private GameProfile profile;

    @Inject(at = @At("RETURN"), method = "getSkinLocation", cancellable = true)
    public void getSkinLocation(CallbackInfoReturnable<ResourceLocation> ci) {
        var original = ci.getReturnValue();
        ci.setReturnValue(PlayerSkinHandler.getCurrentSkin(this.profile, original));
    }

}
