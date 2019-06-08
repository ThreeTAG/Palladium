package com.threetag.threecore.abilities.condition;

import com.google.gson.JsonSyntaxException;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.data.ThreeData;
import com.threetag.threecore.abilities.data.ThreeDataInteger;
import com.threetag.threecore.abilities.data.EnumSync;
import com.threetag.threecore.karma.capability.CapabilityKarma;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.concurrent.atomic.AtomicBoolean;

public class ConditionKarma extends Condition
{

    public static final ThreeData<Integer> MIN = new ThreeDataInteger("min").setSyncType(EnumSync.SELF).enableSetting("min", "The minimum karma required for the condition to be true.");
    public static final ThreeData<Integer> MAX = new ThreeDataInteger("max").setSyncType(EnumSync.SELF).enableSetting("max", "The maximum karma required for the condition to be true.");

    public ConditionKarma(Ability ability)
    {
        super(ConditionType.KARMA, ability);
    }

    @Override public boolean test(EntityLivingBase entity)
    {
        AtomicBoolean b = new AtomicBoolean(false);
        entity.getCapability(CapabilityKarma.KARMA).ifPresent(k -> b.set(k.getKarma() >= this.dataManager.get(MIN) && k.getKarma() <= this.dataManager.get(MAX)));
        return b.get();
    }

    @Override public void registerData()
    {
        super.registerData();
        this.dataManager.register(MIN, Integer.MIN_VALUE);
        this.dataManager.register(MAX, Integer.MAX_VALUE);
    }

    @Override public void deserializeNBT(NBTTagCompound nbt)
    {
        super.deserializeNBT(nbt);
        int min = this.dataManager.get(MIN);
        int max = this.dataManager.get(MAX);
        if (max < min)
            throw new JsonSyntaxException("Max karma value must be lower than min karma value!");
        this.dataManager.set(NAME, min == max ? new TextComponentTranslation("ability.condition.threecore.karma_at", min) : max != Integer.MAX_VALUE ? new TextComponentTranslation("Karma must be between %s and %s", min, max) :
                new TextComponentTranslation("ability.condition.threecore.karma_min", min));
    }
}
