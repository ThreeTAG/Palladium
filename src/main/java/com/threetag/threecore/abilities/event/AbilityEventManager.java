package com.threetag.threecore.abilities.event;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.util.scripts.ScriptManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public class AbilityEventManager implements INBTSerializable<CompoundNBT> {

    public final Ability ability;

    private Map<String, String> subscriptions = Maps.newHashMap();

    public AbilityEventManager(Ability ability) {
        this.ability = ability;
    }

    public void readFromJson(JsonObject jsonObject) {
        if (JSONUtils.hasField(jsonObject, "events")) {
            for (Map.Entry<String, JsonElement> entry : JSONUtils.getJsonObject(jsonObject, "events").entrySet()) {
                this.subscriptions.put(entry.getKey(), entry.getValue().getAsString());
            }
        }
    }

    public void fireEvent(String name, Object... args) {
        if (this.subscriptions.containsKey(name)) {
            ScriptManager.INSTANCE.invoke(this.subscriptions.get(name), args);
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        this.subscriptions.forEach(nbt::putString);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.subscriptions.clear();
        for (String s : nbt.keySet()) {
            this.subscriptions.put(s, nbt.getString(s));
        }
    }
}
