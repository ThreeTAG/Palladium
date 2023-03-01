package net.threetag.palladium.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.PlayerAnimationFrame;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.threetag.palladium.client.model.animation.IAnimatablePlayer;
import net.threetag.palladium.client.model.animation.PalladiumAnimation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin implements IAnimatablePlayer {

    @Unique
    private final ModifierLayer<IAnimation> modifierLayer = new ModifierLayer<>();

    private final Map<PlayerAnimationFrame.PlayerPart, PalladiumAnimation.PartAnimationData> ANIMATION_DATA = new HashMap<>();

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void init(ClientLevel clientLevel, GameProfile gameProfile, @Nullable ProfilePublicKey profilePublicKey, CallbackInfo ci) {
        PlayerAnimationAccess.getPlayerAnimLayer((AbstractClientPlayer) (Object) this).addAnimLayer(1000, modifierLayer);
    }

    @Override
    public ModifierLayer<IAnimation> palladium_getModifierLayer() {
        return this.modifierLayer;
    }

}
