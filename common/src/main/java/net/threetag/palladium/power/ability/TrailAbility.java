package net.threetag.palladium.power.ability;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class TrailAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> TRAIL_RENDERER_ID = new ResourceLocationProperty("trail_renderer").configurable("ID for the trail renderer.");

    public TrailAbility() {
        this.withProperty(TRAIL_RENDERER_ID, new ResourceLocation("example", "trail_renderer_id"));
    }
}
