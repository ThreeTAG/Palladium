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
                double d4 = this.minecraft.options.sensitivity().get() * 0.6F + 0.2F;
                double d5 = d4 * d4 * d4;
                double d6 = d5 * 8.0;
                double dx = this.accumulatedDX * d6;
                double dy = this.accumulatedDY * d6;

                AbilityWheelRenderer.CURRENT_WHEEL.setFromMouseInput(dx, dy);
                this.accumulatedDX = 0;
                this.accumulatedDY = 0;
            }
            ci.cancel();
        }
    }

}
