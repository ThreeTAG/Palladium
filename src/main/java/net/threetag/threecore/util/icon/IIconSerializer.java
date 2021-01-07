package net.threetag.threecore.util.icon;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.TCJsonUtil;

public interface IIconSerializer<T extends IIcon> {

    T read(JsonObject json);

    T read(CompoundNBT nbt);

    CompoundNBT serialize(T icon);

    default CompoundNBT serializeExt(IIcon icon) {
        CompoundNBT nbt = serialize((T) icon);
        nbt.putString("Type", getId().toString());
        return nbt;
    }

    default JsonObject serializeJson(T icon) {
        return new JsonObject();
    }

    default JsonObject serializeJsonExt(IIcon icon) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", getId().toString());
        return TCJsonUtil.merge(jsonObject, this.serializeJson((T) icon));
    }

    ResourceLocation getId();

}
