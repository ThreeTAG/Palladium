package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.power.ability.AbilityDescription;

public class AbilityDescriptionProperty extends PalladiumProperty<AbilityDescription> {

    public AbilityDescriptionProperty(String key) {
        super(key);
    }

    @Override
    public AbilityDescription fromJSON(JsonElement jsonElement) {
        return AbilityDescription.fromJson(jsonElement);
    }

    @Override
    public JsonElement toJSON(AbilityDescription value) {
        return value.toJson();
    }

    @Override
    public AbilityDescription fromNBT(Tag tag, AbilityDescription defaultValue) {
        if (tag instanceof CompoundTag nbt) {
            return AbilityDescription.fromNbt(nbt);
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(AbilityDescription value) {
        return value.toNbt();
    }

    @Override
    public AbilityDescription fromBuffer(FriendlyByteBuf buf) {
        return AbilityDescription.fromBuffer(buf);
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        ((AbilityDescription) value).toBuffer(buf);
    }

    @Override
    public String getString(AbilityDescription value) {
        return value != null ? value.toJson().toString() : null;
    }
}
