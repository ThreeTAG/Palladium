package net.threetag.threecore.scripts.accessors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.AbilityHelper;
import net.threetag.threecore.ability.IAbilityContainer;
import net.threetag.threecore.ability.superpower.Superpower;
import net.threetag.threecore.ability.superpower.SuperpowerManager;
import net.threetag.threecore.scripts.ScriptParameterName;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LivingEntityAccessor extends EntityAccessor
{

	public final LivingEntity livingEntity;

	protected LivingEntityAccessor(LivingEntity entity)
	{
		super(entity);
		this.livingEntity = entity;
	}

	public boolean isChild()
	{
		return this.livingEntity.isChild();
	}

	public float getHealth()
	{
		return this.livingEntity.getHealth();
	}

	public void setHealth(@ScriptParameterName("health") float health)
	{
		this.livingEntity.setHealth(health);
	}

	public float getMaxHealth()
	{
		return this.livingEntity.getMaxHealth();
	}

	public void heal(@ScriptParameterName("amount") float amount)
	{
		this.livingEntity.heal(amount);
	}

	public boolean isUndead()
	{
		return this.livingEntity.isEntityUndead();
	}

	public boolean isOnLadder()
	{
		return this.livingEntity.isOnLadder();
	}

	public boolean isSleeping()
	{
		return this.livingEntity.isSleeping();
	}

	public boolean isElytraFlying()
	{
		return this.livingEntity.isElytraFlying();
	}

	public float getAbsorptionAmount()
	{
		return this.livingEntity.getAbsorptionAmount();
	}

	public float getMovementSpeed()
	{
		return this.livingEntity.getAIMoveSpeed();
	}

	public void setMovementSpeed(@ScriptParameterName("speed") float speed)
	{
		this.livingEntity.setAIMoveSpeed(speed);
	}

	public void swingArm(@ScriptParameterName("mainHand") boolean mainHand)
	{
		this.livingEntity.swing(mainHand ? Hand.MAIN_HAND : Hand.OFF_HAND, true);
	}

	public ItemStackAccessor getItemInSlot(@ScriptParameterName("slot") String slot)
	{
		try
		{
			EquipmentSlotType slotType = EquipmentSlotType.fromString(slot);
			return new ItemStackAccessor(this.livingEntity.getItemStackFromSlot(slotType));
		}
		catch (Exception e)
		{
			return ItemStackAccessor.EMPTY;
		}
	}

	public void setItemInSlot(@ScriptParameterName("slot") String slot, @ScriptParameterName("item") Object item)
	{
		try
		{
			EquipmentSlotType slotType = EquipmentSlotType.fromString(slot);
			ItemStackAccessor stack = item instanceof ItemStackAccessor ? (ItemStackAccessor) item :
					new ItemStackAccessor(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(item.toString()))));
			this.livingEntity.setItemStackToSlot(slotType, stack.value);
		}
		catch (Exception e)
		{

		}
	}

	public AbilityAccessor[] getAbilities()
	{
		List<Ability> list = AbilityHelper.getAbilities(this.livingEntity);
		AbilityAccessor[] array = new AbilityAccessor[list.size()];

		for (int i = 0; i < list.size(); i++)
		{
			array[i] = new AbilityAccessor(list.get(i));
		}

		return array;
	}

	public AbilityAccessor[] getAbilities(@ScriptParameterName("containerId") String containerId)
	{
		IAbilityContainer container = AbilityHelper.getAbilityContainerFromId(this.livingEntity, new ResourceLocation(containerId));

		if (container != null)
			return new AbilityAccessor[0];

		Collection<AbilityAccessor> list = container.getAbilities().stream().map((a) -> new AbilityAccessor(a)).collect(Collectors.toList());
		AbilityAccessor[] array = new AbilityAccessor[list.size()];
		int i = 0;

		for (AbilityAccessor abilityAccessor : list)
		{
			array[i] = abilityAccessor;
			i++;
		}

		return array;
	}

	public AbilityAccessor getAbilityById(@ScriptParameterName("abilityId") String id)
	{
		return (AbilityAccessor) AbilityAccessor.makeAccessor(AbilityHelper.getAbilityById(this.livingEntity, id, null));
	}

	public void attackEntityFrom(@ScriptParameterName("sourceName") String source, @ScriptParameterName("amount") float amount)
	{
		DamageSource damageSource = null;
		switch (source)
		{
		case "inFire":
			damageSource = DamageSource.IN_FIRE;
			break;
		case "lightningBolt":
			damageSource = DamageSource.LIGHTNING_BOLT;
			break;
		case "onFire":
			damageSource = DamageSource.ON_FIRE;
			break;
		case "lava":
			damageSource = DamageSource.LAVA;
			break;
		case "hotFloor":
			damageSource = DamageSource.HOT_FLOOR;
			break;
		case "inWall":
			damageSource = DamageSource.IN_WALL;
			break;
		case "cramming":
			damageSource = DamageSource.CRAMMING;
			break;
		case "drown":
			damageSource = DamageSource.DROWN;
			break;
		case "starve":
			damageSource = DamageSource.STARVE;
			break;
		case "cactus":
			damageSource = DamageSource.CACTUS;
			break;
		case "fall":
			damageSource = DamageSource.FALL;
			break;
		case "flyIntoWall":
			damageSource = DamageSource.FLY_INTO_WALL;
			break;
		case "outOfWorld":
			damageSource = DamageSource.OUT_OF_WORLD;
			break;
		case "generic":
			damageSource = DamageSource.GENERIC;
			break;
		case "magic":
			damageSource = DamageSource.MAGIC;
			break;
		case "wither":
			damageSource = DamageSource.WITHER;
			break;
		case "anvil":
			damageSource = DamageSource.ANVIL;
			break;
		case "fallingBlock":
			damageSource = DamageSource.FALLING_BLOCK;
			break;
		case "dragonBreath":
			damageSource = DamageSource.DRAGON_BREATH;
			break;
		case "fireworks":
			damageSource = DamageSource.FIREWORKS;
			break;
		case "dryout":
			damageSource = DamageSource.DRYOUT;
			break;
		case "sweetBerryBush":
			damageSource = DamageSource.SWEET_BERRY_BUSH;
			break;
		}
		if (damageSource != null)
			this.livingEntity.attackEntityFrom(damageSource, amount);
	}

	public void attackEntityFromMob(@ScriptParameterName("attacker") LivingEntityAccessor entity, @ScriptParameterName("amount") float amount)
	{
		this.livingEntity.attackEntityFrom(DamageSource.causeMobDamage(entity.livingEntity), amount);
	}

	public void attackEntityFromPlayer(@ScriptParameterName("playerAttacker") LivingEntityAccessor entity, @ScriptParameterName("amount") float amount)
	{
		if (entity.livingEntity instanceof PlayerEntity)
			this.livingEntity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) entity.livingEntity), amount);
	}

	public void attackEntityFromThrowable(@ScriptParameterName("throwable") EntityAccessor entity,
			@ScriptParameterName("@Nullable thrower") EntityAccessor thrower, @ScriptParameterName("float") float amount)
	{
		this.livingEntity.attackEntityFrom(DamageSource.causeThrownDamage(entity.value, (thrower == null) ? null : thrower.value), amount);
	}

	public void attackEntityFromExplosion(@ScriptParameterName("@Nullable attacker") LivingEntityAccessor attacker, @ScriptParameterName("float") float amount)
	{
		this.livingEntity.attackEntityFrom(DamageSource.causeExplosionDamage(attacker == null ? null : attacker.livingEntity), amount);
	}

	public void addSuperpower(@ScriptParameterName("superpower") String superpower)
	{
		Superpower s = SuperpowerManager.getInstance().getSuperpower(new ResourceLocation(superpower));
		if (s != null)
			SuperpowerManager.addSuperpower(this.livingEntity, s);
	}
}
