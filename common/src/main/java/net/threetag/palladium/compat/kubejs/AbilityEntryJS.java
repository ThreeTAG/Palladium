package net.threetag.palladium.compat.kubejs;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;

@SuppressWarnings({"rawtypes", "unchecked"})
public record AbilityEntryJS(AbilityEntry abilityEntry) {

    public ResourceLocation getType() {
        return Ability.REGISTRY.getKey(this.abilityEntry.getConfiguration().getAbility());
    }

    public boolean isUnlocked() {
        return this.abilityEntry.isUnlocked();
    }

    public boolean isEnabled() {
        return this.abilityEntry.isEnabled();
    }

    public int getEnabledTicks() {
        return this.abilityEntry.getEnabledTicks();
    }

    public Object getProperty(CharSequence key) {
        PalladiumProperty property = this.abilityEntry.getEitherPropertyByKey(key.toString());

        if (property != null) {
            return this.abilityEntry.getProperty(property);
        } else {
            return null;
        }
    }

    public boolean setProperty(CharSequence key, Object value) {
        PalladiumProperty property = this.abilityEntry.getPropertyManager().getPropertyByName(key.toString());

        if (property != null) {
            this.abilityEntry.setOwnProperty(property, value);
            return true;
        }

        return false;
    }
}
