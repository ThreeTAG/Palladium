package net.threetag.palladium.power.ability;

import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.SyncType;

public class LeapAbility extends Ability {

    public static final PalladiumProperty<Float> MULTIPLIER = new FloatProperty("multiplier").sync(SyncType.NONE).configurable("The leap is based on the player's motion. You can multiplier the strength of it with this property.");

    public LeapAbility() {
        this.withProperty(MULTIPLIER, 1F);
    }

    @Override
    public String getDocumentationDescription() {
        return "Having this ability will make the player do bigger leaps towards their direction when running and jumping.";
    }
}
