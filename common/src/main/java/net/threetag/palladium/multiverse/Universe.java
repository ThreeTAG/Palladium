package net.threetag.palladium.multiverse;

import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionEnvironment;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.Collections;
import java.util.List;

public class Universe {

    private final ResourceLocation id;
    private final int weight;
    private final List<Condition> conditions;

    public Universe(ResourceLocation id, int weight, List<Condition> conditions) {
        this.id = id;
        this.weight = weight;
        this.conditions = conditions;
    }

    public static Universe fromJson(ResourceLocation id, JsonObject json) {
        int weight = GsonUtil.getAsIntMin(json, "weight", 1, 1);
        List<Condition> conditions = Collections.emptyList();
        if (json.has("conditions")) {
            conditions = ConditionSerializer.listFromJSON(json.get("conditions"), ConditionEnvironment.DATA);
        }
        return new Universe(id, weight, conditions);
    }

    public static Universe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        return new Universe(id, buf.readInt(), Collections.emptyList());
    }

    public void toNetwork(FriendlyByteBuf buf) {
        buf.writeInt(this.weight);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Component getTitle() {
        return Component.translatable(makeDescriptionId(this.id));
    }

    public int getWeight() {
        return this.weight;
    }

    public boolean isAvailable(DataContext context) {
        return ConditionSerializer.checkConditions(this.conditions, context);
    }

    public static String makeDescriptionId(ResourceLocation id) {
        return Util.makeDescriptionId("palladium.universe", id);
    }

    public static Component getGenericUniverseComponent(String universe) {
        return Component.translatable(makeDescriptionId(Palladium.id("generic")), universe);
    }

}
