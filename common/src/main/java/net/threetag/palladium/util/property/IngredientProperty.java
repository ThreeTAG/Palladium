package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientProperty extends PalladiumProperty<Ingredient> {

    public IngredientProperty(String key) {
        super(key);
    }

    @Override
    public Ingredient fromJSON(JsonElement jsonElement) {
        return Ingredient.fromJson(jsonElement);
    }

    @Override
    public JsonElement toJSON(Ingredient value) {
        return value.toJson();
    }

    @Override
    public Ingredient fromNBT(Tag tag, Ingredient defaultValue) {
        if(tag instanceof StringTag stringTag) {
            return Ingredient.fromJson(GsonHelper.parse(stringTag.getAsString()));
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Ingredient value) {
        return StringTag.valueOf(value.toJson().toString());
    }

    @Override
    public Ingredient fromBuffer(FriendlyByteBuf buf) {
        return Ingredient.fromJson(GsonHelper.parse(buf.readUtf()));
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeUtf(((Ingredient)value).toJson().toString());
    }
}
