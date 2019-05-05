package com.threetag.threecore.abilities;

import net.minecraft.entity.EntityLivingBase;

public abstract class AbilityHeld extends Ability {

    public AbilityHeld(AbilityType type) {
        super(type);
    }

    @Override
    public EnumAbilityType getAbilityType() {
        return EnumAbilityType.HELD;
    }

    @Override
    public void tick(EntityLivingBase entity) {
        super.tick(entity);

        if (this.conditionManager.isUnlocked()) {
            if (this.dataManager.get(Ability.ENABLED)) {
                if (ticks == 0)
                    firstTick(entity);
                ticks++;
                updateTick(entity);

                if (this.dataManager.has(MAX_COOLDOWN) && this.dataManager.get(MAX_COOLDOWN) > 0) {
                    if (this.dataManager.get(COOLDOWN) >= this.dataManager.get(MAX_COOLDOWN))
                        this.dataManager.set(ENABLED, false);
                    else
                        this.dataManager.set(COOLDOWN, this.getDataManager().get(COOLDOWN) + 1);
                }
            } else {
                if (ticks != 0) {
                    lastTick(entity);
                    ticks = 0;
                }

                if (this.dataManager.has(MAX_COOLDOWN) && this.dataManager.get(MAX_COOLDOWN) > 0) {
                    if (this.dataManager.get(COOLDOWN) > 0)
                        this.dataManager.set(COOLDOWN, this.getDataManager().get(COOLDOWN) - 1);
                }
            }
        } else if (ticks != 0) {
            lastTick(entity);
            ticks = 0;
            this.dataManager.set(Ability.ENABLED, false);
        }
    }

    @Override
    public void onKeyPressed(EntityLivingBase entity) {
        this.dataManager.set(ENABLED, true);
        super.onKeyPressed(entity);
    }

    @Override
    public void onKeyReleased(EntityLivingBase entity) {
        this.dataManager.set(ENABLED, false);
        super.onKeyReleased(entity);
    }

    public void updateTick(EntityLivingBase entity) {

    }

    public void firstTick(EntityLivingBase entity) {

    }

    public void lastTick(EntityLivingBase entity) {

    }

}
