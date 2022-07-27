package net.threetag.palladium.mixin.fabric;

import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.threetag.palladium.event.PalladiumEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Shadow public Input input;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/tutorial/Tutorial;onInput(Lnet/minecraft/client/player/Input;)V"))
    private void aiStep(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        PalladiumEvents.MOVEMENT_INPUT_UPDATE.invoker().update(player, this.input);
    }

}
