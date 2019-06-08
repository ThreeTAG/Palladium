package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.data.EnumSync;
import com.threetag.threecore.abilities.data.ThreeData;
import com.threetag.threecore.abilities.data.ThreeDataInteger;
import net.minecraft.entity.EntityLivingBase;

/**
 * Created by Nictogen on 2019-06-08.
 */
public class ConditionCooldown extends Condition
{
	public static final ThreeData<Integer> MAX_COOLDOWN = new ThreeDataInteger("max_cooldown").setSyncType(EnumSync.SELF).enableSetting("cooldown", "Maximum cooldown for using this ability");
	public static final ThreeData<Integer> COOLDOWN = new ThreeDataInteger("cooldown").setSyncType(EnumSync.SELF);

	public ConditionCooldown(Ability ability)
	{
		super(ConditionType.COOLDOWN, ability);
	}

	@Override public void registerData()
	{
		super.registerData();
		this.dataManager.register(MAX_COOLDOWN, 0);
		this.dataManager.register(COOLDOWN, 0);
		this.dataManager.set(ENABLING, true);
	}

	@Override public boolean test(EntityLivingBase entity)
	{
		if(this.dataManager.get(COOLDOWN) > 0){
			this.dataManager.set(COOLDOWN, this.dataManager.get(COOLDOWN) - 1);
			return false;
		} else return true;
	}


	@Override public void lastTick()
	{
		this.dataManager.set(COOLDOWN, this.dataManager.get(MAX_COOLDOWN));
	}
}
