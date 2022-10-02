package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.entity.BodyPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BodyPartListProperty extends PalladiumProperty<List<BodyPart>> {

    public BodyPartListProperty(String key) {
        super(key);
    }

    @Override
    public List<BodyPart> fromJSON(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return Collections.singletonList(BodyPart.fromJson(jsonElement.getAsString()));
        } else {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<BodyPart> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(BodyPart.fromJson(jsonArray.get(i).getAsString()));
            }
            return list;
        }
    }

    @Override
    public JsonElement toJSON(List<BodyPart> value) {
        if (value.size() == 1) {
            return new JsonPrimitive(value.get(0).getName());
        } else {
            JsonArray jsonArray = new JsonArray();
            for (BodyPart bodyPart : value) {
                jsonArray.add(new JsonPrimitive(bodyPart.getName()));
            }
            return jsonArray;
        }
    }

    @Override
    public List<BodyPart> fromNBT(Tag tag, List<BodyPart> defaultValue) {
        if (tag instanceof ListTag listTag) {
            List<BodyPart> list = new ArrayList<>();
            for (int i = 0; i < listTag.size(); i++) {
                list.add(BodyPart.byName(listTag.getString(i)));
            }
            return list;
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(List<BodyPart> value) {
        ListTag listTag = new ListTag();
        for (BodyPart part : value) {
            listTag.add(StringTag.valueOf(part.getName()));
        }
        return listTag;
    }

    @Override
    public List<BodyPart> fromBuffer(FriendlyByteBuf buf) {
        int amount = buf.readInt();
        List<BodyPart> list = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            list.add(BodyPart.byName(buf.readUtf()));
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        var list = (List<BodyPart>) value;
        buf.writeInt(list.size());

        for (BodyPart part : list) {
            buf.writeUtf(part.getName());
        }
    }

    @Override
    public String getString(List<BodyPart> value) {
        return Arrays.toString(value.stream().map(BodyPart::getName).toArray());
    }
}
