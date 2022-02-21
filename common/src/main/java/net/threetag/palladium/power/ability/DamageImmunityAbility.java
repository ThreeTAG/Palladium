package net.threetag.palladium.power.ability;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Items;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringArrayProperty;

public class DamageImmunityAbility extends Ability {

    public static final PalladiumProperty<String[]> DAMAGE_SOURCES = new StringArrayProperty("damage_sources").configurable("Determines which damage sources have no effect on the entity");

    public DamageImmunityAbility() {
        this.withProperty(ICON, new ItemIcon(Items.POTION));
        this.withProperty(DAMAGE_SOURCES, new String[]{"lightningBolt", "cactus"});
    }

    public static boolean isImmuneAgainst(AbilityEntry entry, DamageSource source) {
        if (!entry.isEnabled()) {
            return false;
        }

        for (String s : entry.getProperty(DAMAGE_SOURCES)) {
            if (s.equals(source.getMsgId())) {
                return true;
            }
        }
        return false;
    }
}
