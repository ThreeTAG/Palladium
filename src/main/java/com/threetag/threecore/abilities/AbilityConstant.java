package com.threetag.threecore.abilities;

import com.threetag.threecore.abilities.data.EnumSync;
import net.minecraft.entity.EntityLivingBase;

public abstract class AbilityConstant extends Ability {

    public AbilityConstant(AbilityType type) {
        super(type);
    }

    @Override
    public void tick(EntityLivingBase entity) {
        if (isUnlocked()) {
            if (getAbilityType() == EnumAbilityType.CONSTANT) {
                ticks++;
                updateTick(entity);
            }
        } else if (ticks != 0) {
            lastTick(entity);
            ticks = 0;
        }

        if (this.dataManager.sync != null) {
            this.sync = this.sync.add(this.dataManager.sync);
            this.dataManager.sync = EnumSync.NONE;
        }
    }

    public abstract void updateTick(EntityLivingBase entity);

    public void lastTick(EntityLivingBase entity) {

    }

    @Override
    public EnumAbilityType getAbilityType() {
        return EnumAbilityType.CONSTANT;
    }
}
