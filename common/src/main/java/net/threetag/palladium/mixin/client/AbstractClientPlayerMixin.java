package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.threetag.palladium.client.renderer.entity.skin.PlayerSkinHandler;
import net.threetag.palladium.client.util.PlayerModelCacheExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin implements PlayerModelCacheExtension {

    @Unique
    private final PlayerModel palladium$cachedModel = new PlayerModel(new ModelPart(Collections.emptyList(), Collections.emptyMap()), false);

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    public void getSkin(CallbackInfoReturnable<PlayerSkin> cir) {
        var originalSkin = cir.getReturnValue();
        cir.setReturnValue(PlayerSkinHandler.getCurrentSkin((AbstractClientPlayer) (Object) this, originalSkin));
    }

    @Override
    public PlayerModel palladium$getCachedModel() {
        return palladium$cachedModel;
    }
}
