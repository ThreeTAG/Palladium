package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class ResourceLocationListProperty extends PalladiumProperty<List<ResourceLocation>> {

    public ResourceLocationListProperty(String key) {
        super(key);
    }

    @Override
    public List<ResourceLocation> fromJSON(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return Collections.singletonList(new ResourceLocation(jsonElement.getAsString()));
        } else {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<ResourceLocation> resourceLocations = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                resourceLocations.add(new ResourceLocation(jsonArray.get(i).getAsString()));
            }
            return resourceLocations;
        }
    }

    @Override
    public JsonElement toJSON(List<ResourceLocation> value) {
        JsonArray jsonArray = new JsonArray();
        for (ResourceLocation rl : value) {
            jsonArray.add(rl.toString());
        }
        return jsonArray;
    }

    @Override
    public List<ResourceLocation> fromNBT(Tag tag, List<ResourceLocation> defaultValue) {
        if (tag instanceof ListTag listTag) {
            List<ResourceLocation> list = new ArrayList<>();
            for (int i = 0; i < listTag.size(); i++) {
                list.add(new ResourceLocation(listTag.getString(i)));
            }
            return list;
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(List<ResourceLocation> value) {
        ListTag listTag = new ListTag();
        for (ResourceLocation s : value) {
            listTag.add(StringTag.valueOf(s.toString()));
        }
        return listTag;
    }

    @Override
    public List<ResourceLocation> fromBuffer(FriendlyByteBuf buf) {
        List<ResourceLocation> list = new ArrayList<>();
        int amount = buf.readInt();
        for (int i = 0; i < amount; i++) {
            list.add(buf.readResourceLocation());
        }
        return list;
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        List<ResourceLocation> strings = (List<ResourceLocation>) value;
        buf.writeInt(strings.size());
        for (ResourceLocation resourceLocation : strings) {
            buf.writeResourceLocation(resourceLocation);
        }
    }

    @Override
    public String getString(List<ResourceLocation> value) {
        return value == null ? null : Arrays.toString(value.stream().map(ResourceLocation::toString).toList().toArray());
    }
}
