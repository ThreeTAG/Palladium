package net.threetag.threecore.abilities.condition;

import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.ThreeData;
import net.minecraft.entity.LivingEntity;

/**
 * Created by Nictogen on 2019-06-08.
 */
public class CooldownCondition extends Condition {

    public static final ThreeData<Integer> MAX_COOLDOWN = new IntegerThreeData("max_cooldown").setSyncType(EnumSync.SELF).enableSetting("cooldown", "Maximum cooldown for using this ability");
    public static final ThreeData<Integer> COOLDOWN = new IntegerThreeData("cooldown").setSyncType(EnumSync.SELF);

    public CooldownCondition(Ability ability) {
        super(ConditionType.COOLDOWN, ability);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(MAX_COOLDOWN, 0);
        this.dataManager.register(COOLDOWN, 0);
        this.dataManager.set(ENABLING, true);
    }

    @Override
    public boolean test(LivingEntity entity) {
        if (this.dataManager.get(COOLDOWN) > 0) {
            this.dataManager.set(COOLDOWN, this.dataManager.get(COOLDOWN) - 1);
            return false;
        } else return true;
    }


    @Override
    public void lastTick() {
        this.dataManager.set(COOLDOWN, this.dataManager.get(MAX_COOLDOWN));
    }
}
