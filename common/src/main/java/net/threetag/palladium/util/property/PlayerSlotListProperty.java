package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.util.PlayerSlot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerSlotListProperty extends PalladiumProperty<List<PlayerSlot>> {

    public PlayerSlotListProperty(String key) {
        super(key);
    }

    @Override
    public List<PlayerSlot> fromJSON(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return Collections.singletonList(PlayerSlot.get(jsonElement.getAsString()));
        } else {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<PlayerSlot> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(PlayerSlot.get(jsonArray.get(i).getAsString()));
            }
            return list;
        }
    }

    @Override
    public JsonElement toJSON(List<PlayerSlot> value) {
        JsonArray jsonArray = new JsonArray();
        for (PlayerSlot s : value) {
            jsonArray.add(s.toString());
        }
        return jsonArray;
    }

    @Override
    public List<PlayerSlot> fromNBT(Tag tag, List<PlayerSlot> defaultValue) {
        if (tag instanceof ListTag listTag) {
            List<PlayerSlot> list = new ArrayList<>();
            for (int i = 0; i < listTag.size(); i++) {
                list.add(PlayerSlot.get(listTag.getString(i)));
            }
            return list;
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(List<PlayerSlot> value) {
        ListTag listTag = new ListTag();
        for (PlayerSlot s : value) {
            listTag.add(StringTag.valueOf(s.toString()));
        }
        return listTag;
    }

    @Override
    public List<PlayerSlot> fromBuffer(FriendlyByteBuf buf) {
        List<PlayerSlot> list = new ArrayList<>();
        int amount = buf.readInt();
        for (int i = 0; i < amount; i++) {
            list.add(PlayerSlot.get(buf.readUtf()));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        List<PlayerSlot> strings = (List<PlayerSlot>) value;
        buf.writeInt(strings.size());
        for (PlayerSlot slot : strings) {
            buf.writeUtf(slot.toString());
        }
    }

    @Override
    public String getString(List<PlayerSlot> value) {
        return value == null ? null : Arrays.toString(value.toArray());
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
