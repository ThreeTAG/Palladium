package net.threetag.palladium.entity;

import net.threetag.palladium.client.renderer.renderlayer.RenderLayerStates;
import net.threetag.palladium.power.PowerHandler;

public interface PalladiumLivingEntityExtension {

    PowerHandler palladium$getPowerHandler();

    RenderLayerStates palladium$getRenderLayerStates();

}
