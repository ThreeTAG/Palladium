package net.threetag.palladium.condition;

import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.property.KeyTypeProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public abstract class KeyCondition extends Condition {

    public static final PalladiumProperty<AbilityConfiguration.KeyType> KEY_TYPE = new KeyTypeProperty("key_type").configurable("The type of key that needs to be pressed");

    public final int cooldown;
    public final AbilityConfiguration.KeyType type;

    public KeyCondition(int cooldown, AbilityConfiguration.KeyType type) {
        this.cooldown = cooldown;
        this.type = type;
    }

    @Override
    public boolean needsKey() {
        return true;
    }

    @Override
    public AbilityConfiguration.KeyType getKeyType() {
        return this.type;
    }

    @Override
    public boolean handlesCooldown() {
        return this.cooldown > 0;
    }

}
