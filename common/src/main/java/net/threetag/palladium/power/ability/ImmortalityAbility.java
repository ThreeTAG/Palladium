package net.threetag.palladium.power.ability;

import net.minecraft.world.item.Items;
import net.threetag.palladium.util.icon.ItemIcon;

public class ImmortalityAbility extends Ability {

    public ImmortalityAbility() {
        this.withProperty(ICON, new ItemIcon(Items.TOTEM_OF_UNDYING));
    }

    @Override
    public String getDocumentationDescription() {
        return "Makes you unable to die.";
    }

}
