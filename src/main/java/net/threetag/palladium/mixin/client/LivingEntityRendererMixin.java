package net.threetag.palladium.mixin.client;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "getModelTint", at = @At("HEAD"))
    private void modifyModelTint(LivingEntityRenderState renderState, CallbackInfoReturnable<Integer> cir) {
//        int tint = renderState.getRenderDataOrDefault(PalladiumRenderStateKeys.TINT, -1);
//
//        if (tint != -1) {
//            cir.setReturnValue(tint);
//        }
    }

}
