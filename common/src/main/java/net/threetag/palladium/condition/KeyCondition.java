package net.threetag.palladium.condition;

import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.KeyTypeProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public abstract class KeyCondition extends Condition {

    public static final PalladiumProperty<AbilityConfiguration.KeyType> KEY_TYPE_WITHOUT_SCROLLING = new KeyTypeProperty("key_type", KeyTypeProperty.WITHOUT_SCROLLING).configurable("The type of key that needs to be pressed");
    public static final PalladiumProperty<AbilityConfiguration.KeyType> KEY_TYPE_WITH_SCROLLING = new KeyTypeProperty("key_type", KeyTypeProperty.ALL).configurable("The type of key that needs to be pressed");
    public static final PalladiumProperty<Boolean> NEEDS_EMPTY_HAND = new BooleanProperty("needs_empty_hand").configurable("Whether or not the player needs to have an empty hand");
    public static final PalladiumProperty<Boolean> ALLOW_SCROLLING_DURING_CROUCHING = new BooleanProperty("allow_scrolling_when_crouching").configurable("If you choose scrolling as a key type, you can determine if scrolling is allowed when crouching. For other key types you can completely ignore this setting.");

    public final int cooldown;
    public final AbilityConfiguration.KeyType type;
    public final boolean needsEmptyHand;
    public final boolean allowScrollingWhenCrouching;

    public KeyCondition(int cooldown, AbilityConfiguration.KeyType type, boolean needsEmptyHand, boolean allowScrollingWhenCrouching) {
        this.cooldown = cooldown;
        this.type = type;
        this.needsEmptyHand = needsEmptyHand;
        this.allowScrollingWhenCrouching = allowScrollingWhenCrouching;
    }

    @Override
    public boolean needsKey() {
        return true;
    }

    @Override
    public AbilityConfiguration.KeyType getKeyType() {
        return this.type;
    }

    public boolean needsEmptyHand() {
        return this.needsEmptyHand;
    }

    public boolean allowScrollingWhenCrouching() {
        return this.allowScrollingWhenCrouching;
    }

    @Override
    public boolean handlesCooldown() {
        return this.cooldown > 0;
    }

}
