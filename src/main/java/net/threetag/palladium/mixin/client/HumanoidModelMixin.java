package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.threetag.palladium.client.animation.BuiltinAnimations;
import net.threetag.palladium.client.animation.PalladiumAnimation;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.logic.context.DataContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin<T extends HumanoidRenderState> {

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("HEAD"))
    public void setupAnimPre(T humanoidRenderState, CallbackInfo ci) {
        var model = (HumanoidModel<?>) (Object) this;
        model.root().getAllParts().forEach(ModelPart::resetPose);
    }

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V", at = @At("TAIL"))
    public void setupAnimPost(T humanoidRenderState, CallbackInfo ci) {
        var model = (HumanoidModel<?>) (Object) this;
        BuiltinAnimations.setupAnim(model, humanoidRenderState);
        var animations = humanoidRenderState.getRenderData(PalladiumRenderStateKeys.ANIMATIONS);

        if (animations != null) {
            for (Map.Entry<DataContext, PalladiumAnimation> entry : animations.entrySet()) {
                entry.getValue().animate(model, entry.getKey(), humanoidRenderState.partialTick);
            }
        }
    }

}