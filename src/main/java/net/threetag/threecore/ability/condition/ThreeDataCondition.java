package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.util.threedata.*;

public class ThreeDataCondition extends Condition
{
	public static final ThreeData<String> TEST_ABILITY = new StringThreeData("test_ability").setSyncType(EnumSync.SELF)
			.enableSetting("test_ability", "The ability to test the threedata from");
	public static final ThreeData<String> DATA_NAME = new StringThreeData("data_name").setSyncType(EnumSync.SELF)
			.enableSetting("data_name", "The name of the data to test");
	public static final ThreeData<Float> ADD_AMOUNT = new FloatThreeData("add_amount").setSyncType(EnumSync.SELF)
			.enableSetting("add_amount", "The amount of data to add after checking");
	public static final ThreeData<String> TEST_VALUE = new StringThreeData("test_value").setSyncType(EnumSync.SELF)
			.enableSetting("test_value", "The value to test the threedata's value against, as a string. Supports string, boolean, float and integer");
	public static final ThreeData<String> TEST_TYPE = new StringThreeData("test_type").setSyncType(EnumSync.SELF)
			.enableSetting("test_type", "The type of test for the value, GREATER, LESS, or EQUAL");
	public static final ThreeData<Float> AMOUNT_MIN = new FloatThreeData("add_amount").setSyncType(EnumSync.SELF)
			.enableSetting("amount_min", "The minimum amount the data can be after adding add_amount");
	public static final ThreeData<Float> AMOUNT_MAX = new FloatThreeData("add_amount").setSyncType(EnumSync.SELF)
			.enableSetting("amount_max", "The maximum amount the data can be after adding add_amount");



	public ThreeDataCondition(Ability ability)
	{
		super(ConditionType.THREE_DATA, ability);
	}

	@Override
	public boolean test(LivingEntity entity)
	{
		try
		{
			TestType testType = TestType.valueOf(getDataManager().get(TEST_TYPE).toUpperCase());
			for (Ability a : AbilityHelper.getAbilities(entity))
			{
				if (a.getId().equals(this.dataManager.get(TEST_ABILITY)))
				{
					ThreeData<?> t = a.getDataManager().getDataByName(this.dataManager.get(DATA_NAME));
					if (t != null)
					{
						if (t instanceof BooleanThreeData || t instanceof StringThreeData)
						{
							if (testType == TestType.EQUAL && a.getDataManager().get(t).toString().equals(this.dataManager.get(TEST_VALUE)))
								return true;
						}
						else if (t instanceof FloatThreeData)
						{
							float testValue = Float.parseFloat(this.dataManager.get(TEST_VALUE));
							float realValue = a.getDataManager().get((FloatThreeData) t);
							switch (testType)
							{
							case GREATER:
								return realValue > testValue;
							case LESS:
								return realValue < testValue;
							case EQUAL:
								return realValue == testValue;
							}
						}
						else if (t instanceof IntegerThreeData)
						{
							int testValue = Integer.parseInt(this.dataManager.get(TEST_VALUE));
							int realValue = a.getDataManager().get((IntegerThreeData) t);
							switch (testType)
							{
							case GREATER:
								return realValue > testValue;
							case LESS:
								return realValue < testValue;
							case EQUAL:
								return realValue == testValue;
							}
						}
					}
				}
			}
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override public void whileEnabled(LivingEntity entity)
	{
		for (Ability a : AbilityHelper.getAbilities(entity))
		{
			if (a.getId().equals(this.dataManager.get(TEST_ABILITY)))
			{
				ThreeData<?> t = a.getDataManager().getDataByName(this.dataManager.get(DATA_NAME));
				if (t instanceof FloatThreeData){
					float addAmount = this.dataManager.get(ADD_AMOUNT);
					float newAmount = MathHelper.clamp(a.getDataManager().get((FloatThreeData) t) + addAmount, this.dataManager.get(AMOUNT_MIN), this.dataManager.get(AMOUNT_MAX));
					a.getDataManager().set((FloatThreeData) t, newAmount);
				} else if(t instanceof IntegerThreeData){
					int addAmount = this.dataManager.get(ADD_AMOUNT).intValue();
					int newAmount = MathHelper.clamp(a.getDataManager().get((IntegerThreeData) t) + addAmount, this.dataManager.get(AMOUNT_MIN).intValue(), this.dataManager.get(AMOUNT_MAX).intValue());
					a.getDataManager().set((IntegerThreeData) t, newAmount);
				}
			}
		}
	}

	@Override
	public void registerData()
	{
		super.registerData();
		this.dataManager.register(TEST_ABILITY, "");
		this.dataManager.register(DATA_NAME, "");
		this.dataManager.register(ADD_AMOUNT, 0f);
		this.dataManager.register(TEST_VALUE, "");
		this.dataManager.register(TEST_TYPE, "GREATER");
		this.dataManager.register(AMOUNT_MAX, Float.MAX_VALUE);
		this.dataManager.register(AMOUNT_MIN, 0f);
	}

	enum TestType
	{
		GREATER,
		LESS,
		EQUAL
	}
}
