package net.threetag.palladium.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.threetag.palladium.client.screen.AbilityWheelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow
    private double xpos;

    @Shadow
    private double ypos;

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private double accumulatedDX;

    @Shadow
    private double accumulatedDY;

    @Inject(method = "turnPlayer", at = @At("HEAD"), cancellable = true)
    public void turnPlayer(CallbackInfo ci) {
        if (AbilityWheelRenderer.CURRENT_WHEEL != null) {
            if (this.accumulatedDX != 0 || this.accumulatedDY != 0) {
                AbilityWheelRenderer.CURRENT_WHEEL.setFromMouseInput(this.accumulatedDX, this.accumulatedDY);
                this.accumulatedDX = 0;
                this.accumulatedDY = 0;
            }
            ci.cancel();
        }
    }

}
