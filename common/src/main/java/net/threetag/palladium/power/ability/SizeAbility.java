package net.threetag.palladium.power.ability;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.icon.TexturedIcon;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class SizeAbility extends Ability {

    public static PalladiumProperty<Float> SCALE = new FloatProperty("scale").configurable("Determines the scale as a multiplier");

    public SizeAbility() {
        this.withProperty(SCALE, 0.1F);
        this.withProperty(ICON, new TexturedIcon(Palladium.id("textures/icon/size.png")));
    }

    @Override
    public String getDocumentationDescription() {
        return "This ability allows an entity to change it's size.";
    }
}
