package net.threetag.threecore.ability.condition;

import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.util.threedata.BooleanThreeData;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ThreeData;
import net.minecraft.entity.LivingEntity;

public abstract class BuyableAbilityCondition extends Condition {

    public static ThreeData<Boolean> BOUGHT = new BooleanThreeData("bought").setSyncType(EnumSync.EVERYONE);

    public BuyableAbilityCondition(ConditionType type, Ability ability) {
        super(type, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(BOUGHT, false);
    }

    /**
     * @param entity
     * @return Returns true if the object that is required to activate the condition is available in the player
     */
    public abstract boolean isAvailable(LivingEntity entity);

    /**
     * Takes the required object from the entity to active the condition
     *
     * @param entity
     * @return Returns true if it was successful
     */
    public abstract boolean takeFromEntity(LivingEntity entity);

    public void buy(LivingEntity entity) {
        if (isAvailable(entity) && takeFromEntity(entity)) {
            this.dataManager.set(BOUGHT, true);
        }
    }

    @Override
    public boolean test(LivingEntity entity) {
        return this.dataManager.get(BOUGHT);
    }
}
