package com.threetag.threecore.abilities;

import net.minecraft.entity.EntityLivingBase;

public abstract class AbilityAction extends Ability {

    public AbilityAction(AbilityType type) {
        super(type);
    }

    @Override
    public EnumAbilityType getAbilityType() {
        return EnumAbilityType.ACTION;
    }

    @Override
    public void tick(EntityLivingBase entity) {
        if (isUnlocked()) {
            if (this.dataManager.get(ENABLED)) {
                if (ticks == 0)
                    firstTick(entity);
                ticks++;
                updateTick(entity);
            } else {
                if (ticks != 0) {
                    lastTick(entity);
                    ticks = 0;
                }

                if (this.dataManager.has(MAX_COOLDOWN) && this.dataManager.get(MAX_COOLDOWN) > 0) {
                    if (this.dataManager.get(COOLDOWN) > 0)
                        this.dataManager.set(COOLDOWN, this.dataManager.get(COOLDOWN) - 1);
                    else
                        this.dataManager.set(ENABLED, true);
                }
            }
        } else if (ticks != 0) {
            lastTick(entity);
            ticks = 0;
        }
    }

    public abstract boolean action(EntityLivingBase entity);

    @Override
    public void onKeyPressed(EntityLivingBase entity) {
        if (this.dataManager.has(MAX_COOLDOWN) && this.dataManager.get(MAX_COOLDOWN) > 0) {
            if (this.dataManager.get(COOLDOWN) == 0) {
                if (this.action(entity)) {
                    this.dataManager.set(ENABLED, false);
                    this.dataManager.set(COOLDOWN, this.dataManager.get(MAX_COOLDOWN));
                    super.onKeyPressed(entity);
                }
            }
        } else {
            this.action(entity);
            super.onKeyPressed(entity);
        }

    }

    @Override
    public void onKeyReleased(EntityLivingBase entity) {
        super.onKeyReleased(entity);
    }

    public void updateTick(EntityLivingBase entity) {

    }

    public void firstTick(EntityLivingBase entity) {

    }

    public void lastTick(EntityLivingBase entity) {

    }
}
