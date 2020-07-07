package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.FloatThreeData;
import net.threetag.threecore.util.threedata.StringThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class LivingValuesCondition extends Condition
{
	public static final ThreeData<String> VALUE_TYPE = new StringThreeData("value_type").setSyncType(EnumSync.SELF).enableSetting("value_type",
			"The type of value to test, HEALTH, HUNGER, SATURATION, OXYGEN, ARMOR, TOUGHNESS, EXPERIENCE_BAR, or EXPERIENCE_POINT");
	public static final ThreeData<String> TEST_TYPE = new StringThreeData("test_type").setSyncType(EnumSync.SELF)
			.enableSetting("test_type", "The type of test for the value, GREATER, LESS, EQUAL, PERCENT_GREATER, PERCENT_LESS, PERCENT_EQUAL");
	public static final ThreeData<Float> TEST_VALUE = new FloatThreeData("test_value").setSyncType(EnumSync.SELF)
			.enableSetting("test_value", "The value to test the entity's value against");

	public LivingValuesCondition(Ability ability)
	{
		super(ConditionType.LIVING_VALUES, ability);
	}

	@Override
	public boolean test(LivingEntity entity)
	{
		try
		{
			float value = 0;
			float maxValue = 0;
			float testValue = getDataManager().get(TEST_VALUE);
			ValueType valueType = ValueType.valueOf(getDataManager().get(VALUE_TYPE).toUpperCase());
			TestType testType = TestType.valueOf(getDataManager().get(TEST_TYPE).toUpperCase());
			switch (valueType)
			{
			case HEALTH:
				value = entity.getHealth();
				maxValue = entity.getMaxHealth();
				break;
			case HUNGER:
				if (entity instanceof PlayerEntity)
					value = ((PlayerEntity) entity).getFoodStats().getFoodLevel();
				maxValue = 20;
				break;
			case SATURATION:
				if (entity instanceof PlayerEntity)
					value = ((PlayerEntity) entity).getFoodStats().getSaturationLevel();
				maxValue = 20;
				break;
			case OXYGEN:
				value = entity.getAir();
				maxValue = entity.getMaxAir();
				break;
			case ARMOR:
				value = entity.getTotalArmorValue();
				maxValue = 20;
				break;
			case TOUGHNESS:
				value = (float) entity.getAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getValue();
				maxValue = 12;
				break;
			case EXPERIENCE_BAR:
				if (entity instanceof PlayerEntity)
				{
					value = ((PlayerEntity) entity).experience;
					maxValue = ((PlayerEntity) entity).xpBarCap();
				}
				break;
			case EXPERIENCE_POINT:
				if (entity instanceof PlayerEntity)
					value = ((PlayerEntity) entity).experienceTotal;
				maxValue = Integer.MAX_VALUE;
				break;
			}

			switch (testType)
			{
			case GREATER:
				return value > testValue;
			case LESS:
				return value < testValue;
			case EQUAL:
				return value == testValue;
			case PERCENT_GREATER:
				return (value/maxValue*100) > testValue;
			case PERCENT_LESS:
				return (value/maxValue*100) < testValue;
			case PERCENT_EQUAL:
				return (value/maxValue*100) == testValue;
			}
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void registerData()
	{
		super.registerData();
		this.dataManager.register(VALUE_TYPE, "HEALTH");
		this.dataManager.register(TEST_TYPE, "GREATER");
		this.dataManager.register(TEST_VALUE, 0f);
	}

	enum ValueType
	{
		HEALTH,
		HUNGER,
		SATURATION,
		OXYGEN,
		ARMOR,
		TOUGHNESS,
		EXPERIENCE_BAR,
		EXPERIENCE_POINT,
	}

	enum TestType
	{
		GREATER,
		LESS,
		EQUAL,
		PERCENT_GREATER,
		PERCENT_LESS,
		PERCENT_EQUAL
	}

	@Override
	public ITextComponent createTitle() {
		String valueType = this.dataManager.get(VALUE_TYPE);
		String testType = this.dataManager.get(TEST_TYPE);
		float testValue = this.dataManager.get(TEST_VALUE);

		return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + (this.dataManager.get(INVERT) ? ".not" : ""), valueType, testType, testValue);
	}
}
