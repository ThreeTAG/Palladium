package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.util.EntityDependentInteger;

public class EntityDependentIntegerProperty extends PalladiumProperty<EntityDependentInteger> {

    public EntityDependentIntegerProperty(String key) {
        super(key);
    }

    @Override
    public EntityDependentInteger fromJSON(JsonElement jsonElement) {
        return EntityDependentInteger.fromJson(jsonElement, this.getKey());
    }

    @Override
    public JsonElement toJSON(EntityDependentInteger value) {
        return value.toJson();
    }

    @Override
    public EntityDependentInteger fromNBT(Tag tag, EntityDependentInteger defaultValue) {
        var value = EntityDependentInteger.fromNBT(tag);
        return value != null ? value : defaultValue;
    }

    @Override
    public Tag toNBT(EntityDependentInteger value) {
        return value.toNBT();
    }

    @Override
    public EntityDependentInteger fromBuffer(FriendlyByteBuf buf) {
        return EntityDependentInteger.fromBuffer(buf);
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        ((EntityDependentInteger) value).toBuffer(buf);
    }

    @Override
    public String getString(EntityDependentInteger value) {
        return value == null ? null : value.toJson().toString();
    }
}
