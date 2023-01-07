package net.threetag.palladium.condition;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import org.jetbrains.annotations.Nullable;

public abstract class BuyableCondition extends Condition {

    public static final PalladiumProperty<Boolean> BOUGHT = new BooleanProperty("bought");

    @Override
    public void registerAbilityProperties(AbilityEntry entry, PropertyManager manager) {
        manager.register(BOUGHT, false);
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return entry != null && entry.getProperty(BOUGHT);
    }

    public abstract AbilityConfiguration.UnlockData createData();

    /**
     * @return Returns true if the object that is required to activate the condition is available in the player
     */
    public abstract boolean isAvailable(LivingEntity entity);

    /**
     * Takes the required object from the entity to activate the condition
     *
     * @return Returns true if it was successful
     */
    public abstract boolean takeFromEntity(LivingEntity entity);

    public void buy(LivingEntity entity, AbilityEntry entry) {
        if (isAvailable(entity) && takeFromEntity(entity)) {
            entry.setOwnProperty(BOUGHT, true);
        }
    }
}
