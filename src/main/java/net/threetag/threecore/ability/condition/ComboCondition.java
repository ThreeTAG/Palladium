package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.event.AbilityEnableChangeEvent;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.IntegerThreeData;
import net.threetag.threecore.util.threedata.StringArrayThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class ComboCondition extends Condition
{
	public static final ThreeData<String[]> ABILITIES = new StringArrayThreeData("abilities").setSyncType(EnumSync.SELF)
			.enableSetting("abilities", "The abilities required to use in order to unlock the ability");
	public static final ThreeData<Integer> STEP = new IntegerThreeData("ability_step").setSyncType(EnumSync.SELF);

	public ComboCondition(Ability ability)
	{
		super(ConditionType.COMBO, ability);
	}

	@Override public boolean test(LivingEntity entity)
	{
		return this.get(STEP) == this.get(ABILITIES).length;
	}

	@Override public void registerData()
	{
		super.registerData();
		this.register(ABILITIES, new String[0]);
		this.register(STEP, 0);
	}

	@Mod.EventBusSubscriber
	public static class Handler
	{
		@SubscribeEvent
		public static void onAbilityEnabled(AbilityEnableChangeEvent event)
		{
			if (event.type == AbilityEnableChangeEvent.Type.ENABLED)
				for (Ability ability : AbilityHelper.getAbilities(event.getEntityLiving()))
					if (ability != event.ability)
						for (Condition condition : ability.getConditionManager().conditions.keySet())
							if (condition instanceof ComboCondition)
							{
								String[] abilities = condition.get(ABILITIES);
								int step = condition.get(STEP);
								if (step < abilities.length && event.ability.getId().equals(abilities[step]))
									condition.set(STEP, condition.get(STEP) + 1);
								else condition.set(STEP, 0);
							}
		}
	}
}
