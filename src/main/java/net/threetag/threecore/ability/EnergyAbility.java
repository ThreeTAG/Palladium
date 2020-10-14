package net.threetag.threecore.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.threedata.*;

public class EnergyAbility extends Ability
{
	public static ThreeData<Integer> AMOUNT = new IntegerThreeData("amount").setSyncType(EnumSync.SELF);
	public static ThreeData<Integer> MAX_AMOUNT = new IntegerThreeData("max_amount").setSyncType(EnumSync.SELF).enableSetting("max_amount", "Sets the maximum amount of energy");
	public static ThreeData<Integer> CHARGE_AMOUNT = new IntegerThreeData("charge_amount").setSyncType(EnumSync.SELF).enableSetting("charge_amount", "Sets the amount of energy created when the ability is enabled");
	public static ThreeData<Integer[]> ENERGY_TEXTURE_INFO = new IntegerArrayThreeData("energy_texture_info").setSyncType(EnumSync.SELF).enableSetting("energy_texture_info", "Data to create the energy texture (x position, y position, x minimum, y minimum, x maximum, y maximum");
	public static ThreeData<Integer[]> BASE_TEXTURE_INFO = new IntegerArrayThreeData("base_texture_info").setSyncType(EnumSync.SELF).enableSetting("base_texture_info", "Data to create the base texture (x position, y position, x size, y size");
	public static ThreeData<ResourceLocation> ENERGY_TEXTURE = new ResourceLocationThreeData("energy_texture").setSyncType(EnumSync.SELF).enableSetting("energy_texture", "Path to the energy texture");
	public static ThreeData<ResourceLocation> BASE_TEXTURE = new ResourceLocationThreeData("base_texture").setSyncType(EnumSync.SELF).enableSetting("base_texture", "Path to the base texture");

	public EnergyAbility()
	{
		super(AbilityType.ENERGY);
	}

	@Override
	public void action(LivingEntity entity) {
		this.dataManager.set(AMOUNT, Math.min(this.dataManager.get(MAX_AMOUNT), this.dataManager.get(AMOUNT) + this.dataManager.get(CHARGE_AMOUNT)));
	}

	@Override
	public void registerData() {
		super.registerData();
		this.dataManager.register(AMOUNT, 0);
		this.dataManager.register(MAX_AMOUNT, 100);
		this.dataManager.register(CHARGE_AMOUNT, 1);
		this.dataManager.register(ENERGY_TEXTURE_INFO, new Integer[]{-13, 3, 10, 0, 10, -100});
		this.dataManager.register(BASE_TEXTURE_INFO, new Integer[]{-16, 0, 16, 106});
		this.dataManager.register(ENERGY_TEXTURE, new ResourceLocation("minecraft:textures/block/blue_wool.png"));
		this.dataManager.register(BASE_TEXTURE, new ResourceLocation("minecraft:textures/block/gray_wool.png"));
	}
}
