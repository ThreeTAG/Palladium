package net.threetag.palladium.power.ability;

import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.util.property.BodyPartListProperty;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.Arrays;
import java.util.List;

public class RemoveBodyPartAbility extends Ability {

    public static final PalladiumProperty<List<BodyPart>> BODY_PARTS = new BodyPartListProperty("body_parts").configurable("Determines which body parts should be completely removed. Available parts: " + Arrays.toString(Arrays.stream(BodyPart.values()).map(BodyPart::getName).toArray()));
    public static final PalladiumProperty<Boolean> AFFECTS_FIRST_PERSON = new BooleanProperty("affects_first_person").configurable("Determines if your first person arm should disappear aswell (if it's disabled)");

    public RemoveBodyPartAbility() {
        this.withProperty(BODY_PARTS, Arrays.asList(BodyPart.RIGHT_ARM, BodyPart.LEFT_ARM));
        this.withProperty(AFFECTS_FIRST_PERSON, true);
    }

    @Override
    public boolean isEffect() {
        return true;
    }

    @Override
    public String getDocumentationDescription() {
        return "Allows you to remove an entity's body parts.";
    }
}
