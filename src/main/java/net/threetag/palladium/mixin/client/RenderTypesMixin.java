package net.threetag.palladium.mixin.client;

import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.client.AbilityClientEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderTypes.class)
public class RenderTypesMixin {

    @Inject(method = "entitySolid", at = @At("HEAD"), cancellable = true)
    private static void entitySolid(Identifier location, CallbackInfoReturnable<RenderType> cir) {
        if (AbilityClientEventHandler.OVERRIDDEN_OPACITY < 1F) {
            cir.setReturnValue(RenderTypes.entityTranslucent(location));
        }
    }

    @Inject(method = "armorCutoutNoCull", at = @At("HEAD"), cancellable = true)
    private static void armorCutoutNoCull(Identifier location, CallbackInfoReturnable<RenderType> cir) {
        if (AbilityClientEventHandler.OVERRIDDEN_OPACITY < 1F) {
            cir.setReturnValue(RenderTypes.armorTranslucent(location));
        }
    }

}
