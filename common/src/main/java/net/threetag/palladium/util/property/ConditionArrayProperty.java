package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.power.ability.condition.Condition;
import net.threetag.palladium.power.ability.condition.ConditionSerializer;

public class ConditionArrayProperty extends PalladiumProperty<Condition[]> {

    public ConditionArrayProperty(String key) {
        super(key);
    }

    @Override
    public Condition[] fromJSON(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            return new Condition[]{ConditionSerializer.fromJSON(jsonElement.getAsJsonObject())};
        } else {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            Condition[] conditions = new Condition[jsonArray.size()];

            for (int i = 0; i < jsonArray.size(); i++) {
                conditions[i] = ConditionSerializer.fromJSON(jsonArray.get(i).getAsJsonObject());
            }
            return conditions;
        }
    }

    @Override
    public JsonElement toJSON(Condition[] value) {
        JsonArray array = new JsonArray();
        for (Condition condition : value) {
            array.add(condition.getSerializer().toJson());
        }
        return array;
    }

    @Override
    public Condition[] fromNBT(Tag tag, Condition[] defaultValue) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public Tag toNBT(Condition[] value) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public Condition[] fromBuffer(FriendlyByteBuf buf) {
        throw new RuntimeException("Not supported!");
//        Condition[] conditions = new Condition[buf.readInt()];
//
//        for(int i = 0; i < conditions.length; i++) {
//            conditions.
//        }
//
//        return conditions;
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        throw new RuntimeException("Not supported!");
    }
}
