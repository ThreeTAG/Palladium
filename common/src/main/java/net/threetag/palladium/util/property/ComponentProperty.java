package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;

public class ComponentProperty extends PalladiumProperty<Component> {

    public ComponentProperty(String key) {
        super(key);
    }

    @Override
    public Component fromJSON(JsonElement jsonElement) {
        return Component.Serializer.fromJson(jsonElement);
    }

    @Override
    public JsonElement toJSON(Component value) {
        return Component.Serializer.toJsonTree(value);
    }

    @Override
    public Component fromNBT(Tag tag, Component defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return Component.Serializer.fromJson(stringTag.getAsString());
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(Component value) {
        return StringTag.valueOf(Component.Serializer.toJson(value));
    }

    @Override
    public Component fromBuffer(FriendlyByteBuf buf) {
        return buf.readComponent();
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeComponent((Component) value);
    }
}
