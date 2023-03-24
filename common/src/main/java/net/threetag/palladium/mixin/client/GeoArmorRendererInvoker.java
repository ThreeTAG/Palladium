package net.threetag.palladium.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mixin(GeoArmorRenderer.class)
public interface GeoArmorRendererInvoker {

    @Invoker("fitToBiped")
    void invokeFitToBiped();

}
