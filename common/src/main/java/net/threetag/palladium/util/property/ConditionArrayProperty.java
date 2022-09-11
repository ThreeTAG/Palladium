package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;

public class ConditionArrayProperty extends PalladiumProperty<Condition[]> {

    public ConditionArrayProperty(String key) {
        super(key);
    }

    @Override
    public Condition[] fromJSON(JsonElement jsonElement) {
        var conditions = ConditionSerializer.listFromJSON(jsonElement, ConditionSerializer.CURRENT_CONTEXT);
        return conditions.toArray(new Condition[0]);
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
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        throw new RuntimeException("Not supported!");
    }

    @Override
    public String getString(Condition[] value) {
        if (value == null) {
            return null;
        } else {
            return value.length + " conditions";
        }
    }
}
