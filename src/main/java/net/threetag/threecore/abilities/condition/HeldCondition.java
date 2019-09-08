package net.threetag.threecore.abilities.condition;

import net.threetag.threecore.abilities.Ability;

/**
 * Created by Nictogen on 2019-06-08.
 */
public class HeldCondition extends KeyboundCondition
{
	public HeldCondition(Ability ability)
	{
		super(ConditionType.HELD, ability);
	}

	@Override void onKeyPressed()
	{
		this.dataManager.set(ENABLED, true);
	}

	@Override void onKeyReleased()
	{
		this.dataManager.set(ENABLED, false);
	}
}
