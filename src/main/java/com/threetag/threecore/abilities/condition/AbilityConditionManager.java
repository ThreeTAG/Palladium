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
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class AbilityConditionManager implements INBTSerializable<NBTTagCompound> {

    public final Ability ability;
    protected boolean unlocked = true;
    protected ArrayList<AbilityCondition> conditions = Lists.newArrayList();
    protected ArrayList<Boolean> active = Lists.newArrayList();

    public AbilityConditionManager(Ability ability) {
        this.ability = ability;
    }

    public void readFromJson(JsonObject jsonObject) {
        if (JsonUtils.hasField(jsonObject, "conditions")) {
            JsonArray jsonArray = JsonUtils.getJsonArray(jsonObject, "conditions");
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonCondition = jsonElement.getAsJsonObject();
                AbilityCondition condition = AbilityConditionSerializer.deserialize(jsonCondition);
                this.addCondition(condition);
            }
        }
    }

    public AbilityConditionManager addCondition(AbilityCondition condition) {
        this.conditions.add(condition);
        this.active.add(false);
        return this;
    }

    public void update(EntityLivingBase entity) {
        if (!entity.world.isRemote) {
            boolean u = true;
            int k = this.conditions.size();
            for (int i = 0; i < k; i++) {
                AbilityCondition condition = this.conditions.get(i);
                boolean active = this.active.get(i);
                boolean b = condition.test(this.ability, entity);
                if (b != active) {
                    this.active.set(i, b);
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
        int k = this.conditions.size();
        for (int i = 0; i < k; i++) {
            list.add(Pair.of(this.conditions.get(i).getName(), this.active.get(i)));
        }
        return list;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.putBoolean("Unlocked", this.unlocked);

        NBTTagList list = new NBTTagList();
        int k = this.conditions.size();
        for (int i = 0; i < k; i++) {
            AbilityCondition condition = this.conditions.get(i);
            NBTTagCompound conditionTag = condition.getSerializer().serializeExt(condition);
            conditionTag.putBoolean("Active", this.active.get(i));
            list.add(conditionTag);
        }
        nbt.put("Conditions", list);

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.unlocked = nbt.getBoolean("Unlocked");
        this.conditions = Lists.newArrayList();
        this.active = Lists.newArrayList();
        NBTTagList list = nbt.getList("Conditions", 10);
        for (int i = 0; i < list.size(); i++) {
            NBTTagCompound conditionTag = list.getCompound(i);
            this.conditions.add(AbilityConditionSerializer.deserialize(conditionTag));
            this.active.add(conditionTag.getBoolean("Active"));
        }
    }

    public NBTTagCompound getUpdatePacket() {
        return this.serializeNBT();
    }

    public void readUpdatePacket(NBTTagCompound nbt) {
        this.deserializeNBT(nbt);
    }

}
