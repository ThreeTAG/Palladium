package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;

public class ExclusiveCondition extends Condition
{
	public ExclusiveCondition(Ability ability)
	{
		super(ConditionType.EXCLUSIVE, ability);
	}

	@Override public boolean test(LivingEntity entity)
	{
		for (Ability ability1 : AbilityHelper.getAbilities(entity))
			if(ability1 != this.ability && ability1.getConditionManager().isEnabled() && ability1.getConditionManager().conditions.keySet().stream().anyMatch(condition -> condition instanceof ExclusiveCondition))
				return false;
		return true;
	}
}
