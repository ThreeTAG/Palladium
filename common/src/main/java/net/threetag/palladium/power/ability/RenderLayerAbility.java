package net.threetag.palladium.power.ability;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class RenderLayerAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> RENDER_LAYER = new ResourceLocationProperty("render_layer").configurable("ID of the render that will be rendered");

    public RenderLayerAbility() {
        this.withProperty(RENDER_LAYER, new ResourceLocation("namespace", "render_layer_id"));
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
