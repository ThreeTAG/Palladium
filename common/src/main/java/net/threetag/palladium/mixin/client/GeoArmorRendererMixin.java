package net.threetag.palladium.mixin.client;

import net.minecraft.client.model.geom.ModelPart;
import net.threetag.palladium.compat.geckolib.renderlayer.GeckoRenderLayerModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;

@Mixin(GeoArmorRenderer.class)
public class GeoArmorRendererMixin {

    @Redirect(method = "applyBaseTransformations", at = @At(value = "INVOKE", target = "Lsoftware/bernie/geckolib/util/RenderUtils;matchModelPartRot(Lnet/minecraft/client/model/geom/ModelPart;Lsoftware/bernie/geckolib/core/animatable/model/CoreGeoBone;)V"))
    private void injected(ModelPart from, CoreGeoBone to) {
        RenderUtils.matchModelPartRot(from, to);
        GeckoRenderLayerModel.copyScaleAndVisibility(from, to);
    }

}
