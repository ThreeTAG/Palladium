package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.util.icon.IIcon;
import net.threetag.palladium.util.icon.IconSerializer;

import java.util.Objects;

public class IconProperty extends PalladiumProperty<IIcon> {

    public IconProperty(String key) {
        super(key);
    }

    @Override
    public IIcon fromJSON(JsonElement jsonElement) {
        return IconSerializer.parseJSON(jsonElement.getAsJsonObject());
    }

    @Override
    public JsonElement toJSON(IIcon value) {
        return IconSerializer.serializeJSON(value);
    }

    @Override
    public IIcon fromNBT(Tag tag, IIcon defaultValue) {
        if (tag instanceof CompoundTag compoundTag) {
            return IconSerializer.parseNBT(compoundTag);
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(IIcon value) {
        return IconSerializer.serializeNBT(value);
    }

    @Override
    public IIcon fromBuffer(FriendlyByteBuf buf) {
        return IconSerializer.parseNBT(Objects.requireNonNull(buf.readNbt()));
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeNbt(IconSerializer.serializeNBT((IIcon) value));
    }
}
