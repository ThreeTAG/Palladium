package net.threetag.palladium.util.property;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.util.PlayerSlot;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayerSlotProperty extends PalladiumProperty<PlayerSlot> {

    public PlayerSlotProperty(String key) {
        super(key);
    }

    @Override
    public PlayerSlot fromJSON(JsonElement jsonElement) {
        return PlayerSlot.get(GsonHelper.convertToString(jsonElement, this.getKey()));
    }

    @Override
    public JsonElement toJSON(PlayerSlot value) {
        return new JsonPrimitive(value.toString());
    }

    @Override
    public PlayerSlot fromNBT(Tag tag, PlayerSlot defaultValue) {
        if (tag instanceof StringTag stringTag) {
            return PlayerSlot.get(stringTag.getAsString());
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(PlayerSlot value) {
        return StringTag.valueOf(value.toString());
    }

    @Override
    public PlayerSlot fromBuffer(FriendlyByteBuf buf) {
        return PlayerSlot.get(buf.readUtf());
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        buf.writeUtf(value.toString());
    }

    @Override
    public String getDescription() {
        String desc = super.getDescription();
        if (!desc.endsWith(".")) {
            desc += ".";
        }
        var list = new ArrayList<>(Arrays.stream(EquipmentSlot.values()).map(EquipmentSlot::getName).toList());
        list.add("curios:back");
        list.add("curios:necklace");
        list.add("trinkets:chest/back");
        list.add("trinkets:chest/necklace");
        return desc + " Example values: " + Arrays.toString(list.toArray());
    }
}
