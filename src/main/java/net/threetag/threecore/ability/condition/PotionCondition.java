package net.threetag.threecore.ability.condition;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.StringArrayThreeData;
import net.threetag.threecore.util.threedata.ThreeData;

public class PotionCondition extends Condition
{
	public static final ThreeData<String[]> POTIONS = new StringArrayThreeData("potions").setSyncType(EnumSync.SELF)
			.enableSetting("potions", "The potion effects to test for. If empty, tests if there are any potion effects");

	public PotionCondition(Ability ability)
	{
		super(ConditionType.POTION, ability);
	}

	@Override
	public boolean test(LivingEntity entity)
	{
		String[] potionStrings = getDataManager().get(POTIONS);
		if(potionStrings.length == 0)
			return !entity.getActivePotionEffects().isEmpty();
		Effect[] potions = new Effect[potionStrings.length];
		for (int i = 0; i < potionStrings.length; i++)
			potions[i] = ForgeRegistries.POTIONS.getValue(new ResourceLocation(potionStrings[i]));
		for (Effect potion : potions)
			if (potion != null && entity.getActivePotionEffect(potion) != null)
				return true;
		return false;
	}

	@Override
	public void registerData()
	{
		super.registerData();
		this.dataManager.register(POTIONS, new String[0]);
	}

	@Override
	public ITextComponent createTitle() {
		String[] potions = this.dataManager.get(POTIONS);
		return new TranslationTextComponent(Util.makeTranslationKey("ability.condition", this.type.getRegistryName()) + (this.dataManager.get(INVERT) ? ".not" : ""), (Object[]) potions);
	}
}
