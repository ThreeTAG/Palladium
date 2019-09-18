package net.threetag.threecore.abilities.condition;

import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.util.threedata.BooleanThreeData;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ThreeData;
import net.minecraft.entity.LivingEntity;

/**
 * Created by Nictogen on 2019-06-08.
 */
public abstract class KeyboundCondition extends Condition {

    public static final ThreeData<Boolean> ENABLED = new BooleanThreeData("enabled").setSyncType(EnumSync.SELF);

    public KeyboundCondition(ConditionType type, Ability ability) {
        super(type, ability);
    }

    @Override
    public boolean test(LivingEntity entity) {
        return this.dataManager.get(ENABLED);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ENABLED, false);
        this.dataManager.register(ENABLING, true);
        this.dataManager.register(NEEDS_KEY, true);
    }

    abstract void onKeyPressed();

    abstract void onKeyReleased();

    @Override
    public void lastTick() {
        if (this.dataManager.get(ENABLED))
            this.dataManager.set(ENABLED, false);
    }
}
