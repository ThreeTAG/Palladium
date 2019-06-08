package com.threetag.threecore.abilities;

import com.google.gson.JsonObject;
import com.threetag.threecore.abilities.client.EnumAbilityColor;
import com.threetag.threecore.abilities.condition.AbilityConditionManager;
import com.threetag.threecore.abilities.data.*;
import com.threetag.threecore.util.render.IIcon;
import com.threetag.threecore.util.render.ItemIcon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Ability implements INBTSerializable<NBTTagCompound>, IThreeDataHolder
{
	public static final ThreeData<Boolean> SHOW_IN_BAR = new ThreeDataBoolean("show_in_bar").setSyncType(EnumSync.SELF)
			.enableSetting("show_in_bar", "Determines if this ability should be displayed in the ability bar without a condition that displays it there.");
	public static final ThreeData<Boolean> HIDDEN = new ThreeDataBoolean("hidden").setSyncType(EnumSync.SELF);
	public static final ThreeData<ITextComponent> TITLE = new ThreeDataTextComponent("title").setSyncType(EnumSync.SELF)
			.enableSetting("title", "Allows you to set a custom title for this ability");
	public static final ThreeData<IIcon> ICON = new ThreeDataIcon("icon").setSyncType(EnumSync.SELF)
			.enableSetting("icon", "Lets you customize the icon for the ability");

	protected final AbilityType type;
	String id;
	public IAbilityContainer container;
	protected ThreeDataManager dataManager = new ThreeDataManager(this);
	protected AbilityConditionManager conditionManager = new AbilityConditionManager(this);
	protected int ticks = 0;
	public EnumSync sync = EnumSync.NONE;
	public boolean dirty = false;
	protected NBTTagCompound additionalData;

	public Ability(AbilityType type)
	{
		this.type = type;
		this.registerData();
	}

	public void readFromJson(JsonObject jsonObject)
	{
		this.dataManager.readFromJson(jsonObject);
		this.conditionManager.readFromJson(jsonObject);
	}

	public void registerData()
	{
		this.dataManager.register(SHOW_IN_BAR, false);
		this.dataManager.register(HIDDEN, false);
		this.dataManager.register(TITLE,
				new TextComponentTranslation("ability." + this.type.getRegistryName().getNamespace() + "." + this.type.getRegistryName().getPath()));
		this.dataManager.register(ICON, new ItemIcon(Blocks.BARRIER));
	}

	public NBTTagCompound getAdditionalData()
	{
		if (this.additionalData == null)
			this.additionalData = new NBTTagCompound();
		return additionalData;
	}

	@OnlyIn(Dist.CLIENT)
	public void drawIcon(Minecraft mc, Gui gui, int x, int y)
	{
		if (this.getDataManager().has(ICON))
			this.getDataManager().get(ICON).draw(mc, x, y);
	}

	public void tick(EntityLivingBase entity)
	{
		this.conditionManager.update(entity);
		if (this.conditionManager.isEnabled())
		{
			if (ticks == 0)
			{
				this.conditionManager.firstTick();
				firstTick(entity);
			}
			ticks++;
			updateTick(entity);
		}
		else if (ticks != 0)
		{
			lastTick(entity);
			this.conditionManager.lastTick();
			ticks = 0;
		}
	}

	public void updateTick(EntityLivingBase entity) { }
	public void firstTick(EntityLivingBase entity) { }
	public void lastTick(EntityLivingBase entity) { }

	// TODO Parent ability
	public Ability getParentAbility()
	{
		return null;
	}

	public ThreeDataManager getDataManager()
	{
		return dataManager;
	}

	public AbilityConditionManager getConditionManager()
	{
		return conditionManager;
	}

	public final String getId()
	{
		return id;
	}

	public final IAbilityContainer getContainer()
	{
		return container;
	}

	@Override public void sync(EnumSync sync)
	{
		this.sync = this.sync.add(sync);
	}

	@Override public void setDirty()
	{
		this.dirty = true;
	}

	@OnlyIn(Dist.CLIENT)
	public EnumAbilityColor getColor()
	{
		return EnumAbilityColor.LIGHT_GRAY;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.putString("AbilityType", this.type.getRegistryName().toString());
		nbt.put("Data", this.dataManager.serializeNBT());
		nbt.put("Conditions", this.conditionManager.serializeNBT());
		if (this.additionalData != null)
			nbt.put("AdditionalData", this.additionalData);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		this.dataManager.deserializeNBT(nbt.getCompound("Data"));
		this.conditionManager.deserializeNBT(nbt.getCompound("Conditions"));
		this.additionalData = nbt.getCompound("AdditionalData");
	}

	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.putString("AbilityType", this.type.getRegistryName().toString());
		nbt.put("Data", this.dataManager.getUpdatePacket());
		nbt.put("Conditions", this.conditionManager.getUpdatePacket());
		if (this.additionalData != null)
			nbt.put("AdditionalData", this.additionalData);
		return nbt;
	}

	public void readUpdateTag(NBTTagCompound nbt)
	{
		this.dataManager.readUpdatePacket(nbt.getCompound("Data"));
		this.conditionManager.readUpdatePacket(nbt.getCompound("Conditions"));
		this.additionalData = nbt.getCompound("AdditionalData");
	}
}
