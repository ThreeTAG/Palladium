package net.threetag.threecore.client.renderer.entity.modellayer.predicates;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

public class IntegerNbtPredicate implements IModelLayerPredicate {

    private final String tag;
    private final int min, max;

    public IntegerNbtPredicate(String tag, int min, int max) {
        this.tag = tag;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean test(IModelLayerContext context) {
        CompoundNBT nbt = context.getAsItem() != null && !context.getAsItem().isEmpty() ? context.getAsItem().getOrCreateTag() : context.getAsEntity().getPersistentData();
        int i = nbt.getInt(this.tag);
        return i >= this.min && i <= this.max;
    }

    public static IntegerNbtPredicate parse(JsonObject json) {
        String tag = JSONUtils.getString(json, "nbt_tag");

        if (JSONUtils.hasField(json, "value")) {
            return new IntegerNbtPredicate(tag, JSONUtils.getInt(json, "value"), JSONUtils.getInt(json, "value"));
        } else {
            return new IntegerNbtPredicate(tag, JSONUtils.getInt(json, "min"), JSONUtils.getInt(json, "max"));
        }
    }

}
