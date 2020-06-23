package net.threetag.threecore.ability.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.scripts.events.AbilityDisabledScriptEvent;
import net.threetag.threecore.scripts.events.AbilityEnabledScriptEvent;
import net.threetag.threecore.scripts.events.AbilityLockedScriptEvent;
import net.threetag.threecore.scripts.events.AbilityUnlockedScriptEvent;
import net.threetag.threecore.util.threedata.EnumSync;

import java.util.*;
import java.util.stream.Collectors;

public class AbilityConditionManager implements INBTSerializable<CompoundNBT>
{

	public final Ability ability;
	protected boolean unlocked = true;
	protected boolean enabled = false;

	protected HashMap<Condition, Boolean> conditions = new HashMap<>();

	public AbilityConditionManager(Ability ability)
	{
		this.ability = ability;
	}

	public void readFromJson(JsonObject jsonObject)
	{
		if (JSONUtils.hasField(jsonObject, "conditions"))
		{
			JsonArray jsonArray = JSONUtils.getJsonArray(jsonObject, "conditions");
			for (JsonElement jsonElement : jsonArray)
			{
				JsonObject jsonCondition = jsonElement.getAsJsonObject();
				ConditionType conditionType = ConditionType.REGISTRY.getValue(new ResourceLocation(JSONUtils.getString(jsonCondition, "type")));
				if (conditionType != null)
				{
					Condition condition = conditionType.create(ability);
					condition.readFromJson(jsonCondition);
					this.addCondition(condition);
				}
				else
				{
					throw new JsonParseException("Condition type " + JSONUtils.getString(jsonCondition, "type") + " does not exist!");
				}
			}
		}
	}

	public Set<Condition> getConditions()
	{
		return conditions.keySet();
	}

	public AbilityConditionManager addCondition(Condition condition)
	{
		return addCondition(condition, false);
	}

	public AbilityConditionManager addCondition(Condition condition, boolean active)
	{
		if (condition.getUniqueId() == null)
		{
			UUID uuid = UUID.randomUUID();
			while (getByUniqueId(uuid) != null)
			{
				uuid = UUID.randomUUID();
			}
			condition.id = uuid;
		}
		this.conditions.put(condition, active);
		return this;
	}

	public Condition getByUniqueId(UUID uuid)
	{
		Objects.requireNonNull(uuid);

		for (Condition c : this.getConditions())
		{
			if (c.getUniqueId().equals(uuid))
			{
				return c;
			}
		}

		return null;
	}

	public void update(LivingEntity entity)
	{
		if (!entity.world.isRemote)
		{
			boolean u = true;
			boolean e = true;

			for (Condition condition : this.conditions.keySet())
			{
				boolean active = this.conditions.get(condition);
				boolean b = condition.getDataManager().get(Condition.INVERT) != condition.test(entity);

				if (b != active)
				{
					this.conditions.put(condition, b);
					this.ability.sync = this.ability.sync.add(EnumSync.EVERYONE);
				}

				if (condition.dataManager.get(Condition.ENABLING))
					e = e && b;
				else
					u = u && b;
			}

			if (e != this.enabled)
			{
				if (e && !(new AbilityEnabledScriptEvent(entity, this.ability).fire()))
					this.enabled = true;
				else if (!e && !(new AbilityDisabledScriptEvent(entity, this.ability).fire()))
					this.enabled = false;

				this.ability.sync = this.ability.sync.add(EnumSync.EVERYONE);
			}

			if (u != this.unlocked)
			{
				this.unlocked = u;
				if (this.unlocked)
					new AbilityUnlockedScriptEvent(entity, this.ability).fire();
				else
					new AbilityLockedScriptEvent(entity, this.ability).fire();
				this.ability.sync = this.ability.sync.add(EnumSync.EVERYONE);
			}
		}
	}

	public void firstTick()
	{
		conditions.forEach((condition, aBoolean) -> condition.firstTick());
	}

	public void lastTick()
	{
		conditions.forEach((condition, aBoolean) -> condition.lastTick());
	}

	public void onKeyPressed()
	{
		if (isUnlocked())
			for (Condition condition : this.conditions.keySet())
				if (condition instanceof KeyboundCondition)
					((KeyboundCondition) condition).onKeyPressed();
	}

	public void onKeyReleased()
	{
		if (isUnlocked())
			for (Condition condition : this.conditions.keySet())
				if (condition instanceof KeyboundCondition)
					((KeyboundCondition) condition).onKeyReleased();
	}

	public void disableKeybounds()
	{
		for (Condition condition : this.conditions.keySet())
			if (condition instanceof KeyboundCondition)
				condition.dataManager.set(KeyboundCondition.ENABLED, false);
	}

	public boolean isUnlocked()
	{
		return this.unlocked;
	}

	public boolean isEnabled()
	{
		return isUnlocked() && this.enabled;
	}

	public boolean needsKey()
	{
		for (Condition condition : this.conditions.keySet())
			if (condition.dataManager.get(Condition.NEEDS_KEY))
				return true;
		return false;
	}

	public boolean isActive(Condition condition)
	{
		return this.conditions.get(condition);
	}

	public List<Condition> getFilteredConditions(boolean enabling)
	{
		return this.conditions.keySet().stream().filter(c -> c.getDataManager().get(Condition.ENABLING) == enabling).collect(Collectors.toList());
	}

	public List<Condition> getFilteredConditions(boolean enabling, boolean active)
	{
		return this.conditions.keySet().stream().filter(c -> c.getDataManager().get(Condition.ENABLING) == enabling && this.conditions.get(c) == active)
				.collect(Collectors.toList());
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		CompoundNBT nbt = new CompoundNBT();
		nbt.putBoolean("Unlocked", this.unlocked);
		nbt.putBoolean("Enabled", this.enabled);

		ListNBT list = new ListNBT();
		conditions.forEach((abilityCondition, aBoolean) -> {
			CompoundNBT conditionTag = abilityCondition.serializeNBT();
			conditionTag.putBoolean("Active", aBoolean);
			list.add(conditionTag);
		});
		nbt.put("Conditions", list);

		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt)
	{
		this.unlocked = nbt.getBoolean("Unlocked");
		this.enabled = nbt.getBoolean("Enabled");
		this.conditions = new HashMap<>();
		ListNBT list = nbt.getList("Conditions", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++)
		{
			CompoundNBT conditionTag = list.getCompound(i);
			ConditionType conditionType = ConditionType.REGISTRY.getValue(new ResourceLocation(conditionTag.getString("ConditionType")));
			if (conditionType != null)
			{
				Condition condition = conditionType.create(ability);
				condition.deserializeNBT(conditionTag);
				this.addCondition(condition, conditionTag.getBoolean("Active"));
			}
			else
			{
				ThreeCore.LOGGER.error("Condition type " + conditionTag.getString("ConditionType") + " does not exist!");
			}
		}
	}

	public CompoundNBT getUpdatePacket()
	{
		return this.serializeNBT();
	}

	public void readUpdatePacket(CompoundNBT nbt)
	{
		this.deserializeNBT(nbt);
	}

}
