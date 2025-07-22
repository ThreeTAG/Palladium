package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.threetag.palladium.client.animation.AimAnimation;
import net.threetag.palladium.client.animation.HideBodyPartsAnimation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    public void setupAnim(T humanoidRenderState, CallbackInfo ci) {
        var model = (HumanoidModel<?>) (Object) this;
        AimAnimation.setupAnim(model, humanoidRenderState);
        HideBodyPartsAnimation.setupAnim(model, humanoidRenderState);
    }

}
