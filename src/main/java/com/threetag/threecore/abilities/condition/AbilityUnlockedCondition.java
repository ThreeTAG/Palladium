package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.data.ThreeData;
import com.threetag.threecore.abilities.data.StringThreeData;
import com.threetag.threecore.abilities.data.EnumSync;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AbilityUnlockedCondition extends Condition
{

    public static final ThreeData<String> ABILITY_ID = new StringThreeData("ability_id").setSyncType(EnumSync.SELF).enableSetting("ability_id", "The id for the ability that must be unlocked for the condition to be true.");

    public AbilityUnlockedCondition(Ability ability)
    {
        super(ConditionType.ABILITY_UNLOCKED, ability);
    }

    @Override public void registerData()
    {
        super.registerData();
        this.dataManager.register(ABILITY_ID, "");
    }

    @Override public boolean test(LivingEntity entity)
    {
        Ability dependentAbility = AbilityHelper.getAbilityById(entity, this.dataManager.get(ABILITY_ID), ability.container);
        // Not the best way to handle the name, but it works �\_(?)_/�
        this.dataManager.set(Condition.NAME, dependentAbility == null ? new StringTextComponent("") : new TranslationTextComponent("ability.condition.threecore.ability_unlocked", dependentAbility.getDataManager().get(Ability.TITLE)));
        return dependentAbility != null && dependentAbility != ability && dependentAbility.getConditionManager().isUnlocked();
    }

}
