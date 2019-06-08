package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.data.ThreeData;
import com.threetag.threecore.abilities.data.ThreeDataString;
import com.threetag.threecore.abilities.data.EnumSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class ConditionAbilityEnabled extends Condition
{

    public static final ThreeData<String> ABILITY_ID = new ThreeDataString("ability_id").setSyncType(EnumSync.SELF).enableSetting("ability_id", "The id for the ability that must be enabled for the condition to be true.");

    public ConditionAbilityEnabled(Ability ability)
    {
        super(ConditionType.ABILITY_ENABLED, ability);
    }

    @Override public void registerData()
    {
        super.registerData();
        this.dataManager.register(ABILITY_ID, "");
    }

    @Override public boolean test(EntityLivingBase entity)
    {
        Ability dependentAbility = AbilityHelper.getAbilityById(entity, this.dataManager.get(ABILITY_ID), ability.container);
        // Not the best way to handle the name, but it works �\_(?)_/�
        this.dataManager.set(Condition.NAME, dependentAbility == null ? new TextComponentString("") : new TextComponentTranslation("ability.condition.threecore.ability_enabled", dependentAbility.getDataManager().get(Ability.TITLE)));
        if (dependentAbility == null || dependentAbility == ability)
            return false;
        return !dependentAbility.getDataManager().has(Ability.ENABLED) || dependentAbility.getDataManager().get(Ability.ENABLED);
    }
}
