package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.abilities.Ability;

/**
 * Created by Nictogen on 2019-06-08.
 */
public class ConditionToggle extends ConditionKeybound
{
	public ConditionToggle(Ability ability)
	{
		super(ConditionType.TOGGLE, ability);
	}

	@Override void onKeyPressed()
	{
		this.dataManager.set(ENABLED, !this.dataManager.get(ENABLED));
	}

	@Override void onKeyReleased()
	{

	}
}
