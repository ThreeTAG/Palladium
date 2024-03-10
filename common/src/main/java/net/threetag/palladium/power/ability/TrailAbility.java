package net.threetag.palladium.power.ability;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;

public class TrailAbility extends Ability {

    public static final PalladiumProperty<ResourceLocation> TRAIL_RENDERER_ID = new ResourceLocationProperty("trail").configurable("ID for the trail renderer. Trail configuration files must be under 'assets/<namespace>/palladium/trails/<trail_id>.json'");

    public TrailAbility() {
        this.withProperty(TRAIL_RENDERER_ID, new ResourceLocation("example", "trail_renderer_id"));
    }
}
