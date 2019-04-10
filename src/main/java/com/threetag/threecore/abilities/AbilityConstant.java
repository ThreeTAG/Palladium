package com.threetag.threecore.abilities;

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
    }

    public abstract void updateTick(EntityLivingBase entity);

    public void lastTick(EntityLivingBase entity) {

    }

    @Override
    public boolean needsKey() {
        return false;
    }

    @Override
    public EnumAbilityType getAbilityType() {
        return EnumAbilityType.CONSTANT;
    }
}
