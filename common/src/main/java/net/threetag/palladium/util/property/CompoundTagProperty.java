package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.util.json.GsonUtil;

public class CompoundTagProperty extends PalladiumProperty<CompoundTag> {

    public CompoundTagProperty(String key) {
        super(key);
    }

    @Override
    public CompoundTag fromJSON(JsonElement jsonElement) {
        try {
            return TagParser.parseTag(GsonHelper.convertToJsonObject(jsonElement, this.getKey()).toString());
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonElement toJSON(CompoundTag value) {
        return GsonUtil.nbtToJson(value);
    }

    @Override
    public CompoundTag fromNBT(Tag tag, CompoundTag defaultValue) {
        return tag instanceof CompoundTag compoundTag ? compoundTag : defaultValue;
    }

    @Override
    public Tag toNBT(CompoundTag value) {
        return value;
    }

    @Override
    public CompoundTag fromBuffer(FriendlyByteBuf buf) {
        return buf.readAnySizeNbt();
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeNbt((CompoundTag) value);
    }

    @Override
    public String getString(CompoundTag value) {
        return value == null ? null : this.toJSON(value).toString();
    }

    @Override
    public String getPropertyType() {
        return "compound_tag";
    }
}
