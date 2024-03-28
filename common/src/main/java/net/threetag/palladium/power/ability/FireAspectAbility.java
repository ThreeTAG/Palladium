package net.threetag.palladium.power.ability;

import net.minecraft.world.item.Items;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class FireAspectAbility extends Ability {

    public static final PalladiumProperty<Integer> TIME = new IntegerProperty("time").configurable("The amount of time, in seconds, that the victim entity will be set on fire for");
    public static final PalladiumProperty<Boolean> SHOULD_STACK_TIME = new BooleanProperty("should_stack_time").configurable("If true, attacking an entity that's already on fire will add the \"time\" field to their current burn time instead of setting it");
    public static final PalladiumProperty<Integer> MAX_TIME = new IntegerProperty("max_time").configurable("If \"should_stack_time\" is true, the victim's burn time (in seconds) will not exceed this value after being hit");

    public FireAspectAbility() {
        this.withProperty(ICON, new ItemIcon(Items.FLINT_AND_STEEL));

        this.withProperty(TIME, 5);
        this.withProperty(SHOULD_STACK_TIME, false);
        this.withProperty(MAX_TIME, 5);
    }

    @Override
    public String getDocumentationDescription() {
        return "Makes this entity's attacks light targets on fire as if fire aspect was used.";
    }
}
