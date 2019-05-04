package com.threetag.threecore.abilities.condition;

import com.google.gson.JsonObject;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.karma.capability.CapabilityKarma;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.concurrent.atomic.AtomicBoolean;

public class AbilityConditionKarma extends AbilityCondition {

    public int min;
    public int max;

    public AbilityConditionKarma(ITextComponent name, int min, int max) {
        super(name);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test(Ability ability, EntityLivingBase entity) {
        AtomicBoolean b = new AtomicBoolean(false);
        entity.getCapability(CapabilityKarma.KARMA).ifPresent(k -> {
            b.set(k.getKarma() >= this.min && k.getKarma() <= this.max);
        });
        return b.get();
    }

    @Override
    public IAbilityConditionSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IAbilityConditionSerializer<AbilityConditionKarma> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "karma");

        @Override
        public AbilityConditionKarma read(JsonObject json) {
            ITextComponent name = JsonUtils.hasField(json, "name") ?
                    ITextComponent.Serializer.fromJson(JsonUtils.getJsonObject(json, "name").toString()) :
                    JsonUtils.hasField(json, "max") ? new TextComponentTranslation("Karma must be between %s and %s", JsonUtils.getInt(json, "min"), JsonUtils.getInt(json, "max", Integer.MAX_VALUE)) :
                    new TextComponentTranslation("ability.condition.threecore.karma_min", JsonUtils.getInt(json, "min"));
            return new AbilityConditionKarma(name, JsonUtils.getInt(json, "min"), JsonUtils.getInt(json, "max", Integer.MAX_VALUE));
        }

        @Override
        public AbilityConditionKarma read(NBTTagCompound nbt) {
            ITextComponent name = ITextComponent.Serializer.fromJson(nbt.getString("Name"));
            return new AbilityConditionKarma(name, nbt.getInt("Min"), nbt.getInt("Max"));
        }

        @Override
        public NBTTagCompound serialize(AbilityConditionKarma condition) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.putString("Name", ITextComponent.Serializer.toJson(condition.name));
            nbt.putInt("Min", condition.min);
            nbt.putInt("Max", condition.max);
            return nbt;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }

}
