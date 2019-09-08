package net.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.threetag.threecore.util.render.IIcon;
import net.threetag.threecore.util.render.IconSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class IconThreeData extends ThreeData<IIcon> {

    public IconThreeData(String key) {
        super(key);
    }

    @Override
    public IIcon parseValue(JsonObject jsonObject, IIcon defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return IconSerializer.deserialize(JSONUtils.getJsonObject(jsonObject, this.jsonKey));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, IIcon value) {
        nbt.put(this.key, value.getSerializer().serializeExt(value));
    }

    @Override
    public IIcon readFromNBT(CompoundNBT nbt, IIcon defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        IIcon icon = IconSerializer.deserialize(nbt.getCompound(this.key));
        return icon == null ? defaultValue : icon;
    }

    @Override
    public String getDisplay(IIcon value) {
        // TODO Icon serialized
        return "\"icon\"";
    }
}
