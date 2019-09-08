package net.threetag.threecore.abilities.condition;

import net.threetag.threecore.abilities.Ability;
import net.minecraft.entity.LivingEntity;

/**
 * Created by Nictogen on 2019-06-08.
 */
public class ActionCondition extends KeyboundCondition
{

	public ActionCondition(Ability ability)
	{
		super(ConditionType.ACTION, ability);
	}

	@Override public boolean test(LivingEntity entity)
	{
		if(super.test(entity)){
			this.dataManager.set(ENABLED, false);
			return true;
		}
		return false;
	}

	@Override void onKeyPressed()
	{
		this.dataManager.set(ENABLED, true);
	}

	@Override void onKeyReleased() { }
}
