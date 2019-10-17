package net.threetag.threecore.util.scripts.accessors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.abilities.IAbilityContainer;
import net.threetag.threecore.util.player.PlayerHelper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LivingEntityAccessor extends EntityAccessor {

    public final LivingEntity livingEntity;

    protected LivingEntityAccessor(LivingEntity entity) {
        super(entity);
        this.livingEntity = entity;
    }

    public boolean isChild() {
        return this.livingEntity.isChild();
    }

    public float getHealth() {
        return this.livingEntity.getHealth();
    }

    public void setHealth(float health) {
        this.livingEntity.setHealth(health);
    }

    public float getMaxHealth() {
        return this.livingEntity.getMaxHealth();
    }

    public void heal(float amount) {
        this.livingEntity.heal(amount);
    }

    public boolean isUndead() {
        return this.livingEntity.isEntityUndead();
    }

    public boolean isOnLadder() {
        return this.livingEntity.isOnLadder();
    }

    public boolean isSleeping() {
        return this.livingEntity.isSleeping();
    }

    public boolean isElytraFlying() {
        return this.livingEntity.isElytraFlying();
    }

    public int getIdleTime() {
        return this.getIdleTime();
    }

    public float getAbsorptionAmount() {
        return this.livingEntity.getAbsorptionAmount();
    }

    public float getMovementSpeed() {
        return this.livingEntity.getAIMoveSpeed();
    }

    public void setMovementSpeed(float speed) {
        this.livingEntity.setAIMoveSpeed(speed);
    }

    public AbilityAccessor[] getAbilities() {
        List<Ability> list = AbilityHelper.getAbilities(this.livingEntity);
        AbilityAccessor[] array = new AbilityAccessor[list.size()];

        for (int i = 0; i < list.size(); i++) {
            array[i] = new AbilityAccessor(list.get(i));
        }

        return array;
    }

    public AbilityAccessor[] getAbilities(String containerId) {
        IAbilityContainer container = AbilityHelper.getAbilityContainerFromId(this.livingEntity, new ResourceLocation(containerId));

        if (container != null)
            return new AbilityAccessor[0];

        Collection<AbilityAccessor> list = container.getAbilities().stream().map((a) -> new AbilityAccessor(a)).collect(Collectors.toList());
        AbilityAccessor[] array = new AbilityAccessor[list.size()];
        int i = 0;

        for (AbilityAccessor abilityAccessor : list) {
            array[i] = abilityAccessor;
            i++;
        }

        return array;
    }

    @Override
    public boolean equals(Object obj) {
        return this.livingEntity.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.livingEntity.hashCode();
    }
}
