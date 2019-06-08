package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.abilities.Ability;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 2019-06-08.
 */
public class ConditionAction extends ConditionKeybound
{

	public ConditionAction(Ability ability)
	{
		super(ConditionType.ACTION, ability);
	}

	@Override public boolean test(EntityLivingBase entity)
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
