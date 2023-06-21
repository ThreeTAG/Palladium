package net.threetag.palladium.mixin.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.client.renderer.entity.HumanoidRendererModifications;
import net.threetag.palladium.util.RenderUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderType.class)
public class RenderTypeMixin {

    @Inject(method = "entitySolid", at = @At("HEAD"), cancellable = true)
    private static void entitySolid(ResourceLocation location, CallbackInfoReturnable<RenderType> ci) {
        if (RenderUtil.REDIRECT_GET_BUFFER) {
            ci.setReturnValue(RenderType.entityTranslucent(location));
        }
    }

    @Inject(method = "armorCutoutNoCull", at = @At("HEAD"), cancellable = true)
    private static void armorCutoutNoCull(ResourceLocation location, CallbackInfoReturnable<RenderType> cir) {
        if (HumanoidRendererModifications.ALPHA_MULTIPLIER != 1F) {
            cir.setReturnValue(PalladiumRenderTypes.ARMOR_CUTOUT_NO_CULL_TRANSPARENCY.apply(location));
        }
    }

}
