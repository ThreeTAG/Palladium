package net.threetag.palladium.multiverse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionEnvironment;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Universe {

    private final ResourceLocation id;
    private final int weight;
    private final List<ConditionalWeight> conditionalWeights;

    public Universe(ResourceLocation id, int weight, List<ConditionalWeight> conditionalWeights) {
        this.id = id;
        this.weight = weight;
        this.conditionalWeights = conditionalWeights;
    }

    public static Universe fromJson(ResourceLocation id, JsonObject json) {
        int weight = GsonUtil.getAsIntMin(json, "weight", 0, 1);
        List<ConditionalWeight> conditionalWeights = new ArrayList<>();
        var conditionsWeightsEl = json.get("conditional_weights");
        if (conditionsWeightsEl != null) {
            var array = GsonHelper.convertToJsonArray(conditionsWeightsEl, "conditional_weights");

            for (JsonElement jsonElement : array) {
                conditionalWeights.add(ConditionalWeight.fromJson(GsonHelper.convertToJsonObject(jsonElement, "conditional_weights[].$")));
            }
        }
        return new Universe(id, weight, conditionalWeights);
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

    public int getWeight(DataContext context) {
        for (ConditionalWeight conditionalWeight : this.conditionalWeights) {
            if (ConditionSerializer.checkConditions(conditionalWeight.conditions, context)) {
                return conditionalWeight.weight;
            }
        }
        return this.weight;
    }

    public static String makeDescriptionId(ResourceLocation id) {
        return Util.makeDescriptionId("palladium.universe", id);
    }

    public static Component getGenericUniverseComponent(String universe) {
        return Component.translatable(makeDescriptionId(Palladium.id("generic")), universe);
    }

    public static class ConditionalWeight {

        private final int weight;
        private final List<Condition> conditions;

        public ConditionalWeight(int weight, List<Condition> conditions) {
            this.weight = weight;
            this.conditions = conditions;
        }

        public static ConditionalWeight fromJson(JsonObject json) {
            return new ConditionalWeight(GsonUtil.getAsIntMin(json, "weight", 1),
                    ConditionSerializer.listFromJSON(json.get("if"), ConditionEnvironment.DATA));
        }
    }

}
