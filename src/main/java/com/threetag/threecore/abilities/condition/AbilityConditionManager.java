package com.threetag.threecore.abilities.condition;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.data.EnumSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;

public class AbilityConditionManager implements INBTSerializable<NBTTagCompound> {

    public final Ability ability;
    protected boolean unlocked = true;
    protected HashMap<Condition, Boolean> conditions = new HashMap<>();

    public AbilityConditionManager(Ability ability) {
        this.ability = ability;
    }

    public void readFromJson(JsonObject jsonObject) {
        if (JsonUtils.hasField(jsonObject, "conditions")) {
            JsonArray jsonArray = JsonUtils.getJsonArray(jsonObject, "conditions");
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonCondition = jsonElement.getAsJsonObject();
                ConditionType conditionType = ConditionType.REGISTRY.getValue(new ResourceLocation(JsonUtils.getString(jsonCondition, "type")));
                Condition condition = conditionType.create(ability);
                condition.dataManager.readFromJson(jsonCondition);
                this.addCondition(condition);
            }
        }
    }

    public AbilityConditionManager addCondition(Condition condition) {
        this.conditions.put(condition, false);
        return this;
    }

    public void update(EntityLivingBase entity) {
        if (!entity.world.isRemote) {
            boolean u = true;
            Condition[] conditionArray = this.conditions.keySet().toArray(new Condition[0]);
            for (Condition condition : conditionArray)
            {
                boolean active = this.conditions.get(condition);
                boolean b = condition.test(entity);
                if (b != active) {
                    this.conditions.put(condition, b);
                    this.ability.sync = this.ability.sync.add(EnumSync.EVERYONE);
                }

                u = u && b;

                if (u != this.unlocked) {
                    this.unlocked = u;
                    this.ability.sync = this.ability.sync.add(EnumSync.EVERYONE);
                }
            }
        }
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    public List<Pair<ITextComponent, Boolean>> getConditionStates() {
        List<Pair<ITextComponent, Boolean>> list = Lists.newArrayList();
        this.conditions.forEach((abilityCondition, aBoolean) -> list.add(Pair.of(abilityCondition.dataManager.get(Condition.NAME), aBoolean)));
        return list;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.putBoolean("Unlocked", this.unlocked);

        NBTTagList list = new NBTTagList();
        conditions.forEach((abilityCondition, aBoolean) -> {
            NBTTagCompound conditionTag = abilityCondition.serializeNBT();
            conditionTag.putBoolean("Active", aBoolean);
            list.add(conditionTag);
        });
        nbt.put("Conditions", list);

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.unlocked = nbt.getBoolean("Unlocked");
        this.conditions = new HashMap<>();
        NBTTagList list = nbt.getList("Conditions", 10);
        for (int i = 0; i < list.size(); i++) {
            NBTTagCompound conditionTag = list.getCompound(i);
            ConditionType conditionType = ConditionType.REGISTRY.getValue(new ResourceLocation(conditionTag.getString("ConditionType")));
            this.conditions.put(conditionType.create(ability), conditionTag.getBoolean("Active"));
        }
    }

    public NBTTagCompound getUpdatePacket() {
        return this.serializeNBT();
    }

    public void readUpdatePacket(NBTTagCompound nbt) {
        this.deserializeNBT(nbt);
    }

}
