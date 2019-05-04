package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.ITextComponent;

public abstract class AbilityCondition {

    protected ITextComponent name;

    public AbilityCondition(ITextComponent name) {
        this.name = name;
    }

    public ITextComponent getName() {
        return name;
    }

    public abstract boolean test(Ability ability, EntityLivingBase entity);

    public abstract IAbilityConditionSerializer<?> getSerializer();

    public interface IAbilityPredicate {

        boolean test(Ability ability, EntityLivingBase entity);

    }

}
