package com.threetag.threecore.abilities.condition;

import com.google.gson.JsonObject;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class AbilityConditionAbilityUnlocked extends AbilityCondition {

    protected String abilityId;

    public AbilityConditionAbilityUnlocked(String abilityId) {
        super(new TextComponentString(""));
        this.abilityId = abilityId;
    }

    @Override
    public boolean test(Ability ability, EntityLivingBase entity) {
        Ability dependentAbility = AbilityHelper.getAbilityById(entity, this.abilityId, ability.container);
        // Not the best way to handle the name, but it works ¯\_(?)_/¯
        this.name = dependentAbility == null ? new TextComponentString("") : new TextComponentTranslation("ability.condition.threecore.ability_unlocked", dependentAbility.getDataManager().get(Ability.TITLE));
        return dependentAbility != null && dependentAbility != ability && dependentAbility.getConditionManager().isUnlocked();
    }

    @Override
    public IAbilityConditionSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IAbilityConditionSerializer<AbilityConditionAbilityUnlocked> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "ability_unlocked");

        @Override
        public AbilityConditionAbilityUnlocked read(JsonObject json) {
            return new AbilityConditionAbilityUnlocked(JsonUtils.getString(json, "ability"));
        }

        @Override
        public AbilityConditionAbilityUnlocked read(NBTTagCompound nbt) {
            AbilityConditionAbilityUnlocked condition = new AbilityConditionAbilityUnlocked(nbt.getString("Ability"));
            condition.name = ITextComponent.Serializer.fromJson(nbt.getString("Name"));
            return condition;
        }

        @Override
        public NBTTagCompound serialize(AbilityConditionAbilityUnlocked condition) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.putString("Ability", condition.abilityId);
            nbt.putString("Name", ITextComponent.Serializer.toJson(condition.name));
            return nbt;
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    }

}
