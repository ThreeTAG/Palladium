package net.threetag.palladium.util.property;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagKeyListProperty<T> extends PalladiumProperty<List<TagKey<T>>> {

    private final ResourceKey<Registry<T>> registry;

    public TagKeyListProperty(String key, ResourceKey<Registry<T>> registry) {
        super(key);
        this.registry = registry;
    }

    @Override
    public List<TagKey<T>> fromJSON(JsonElement jsonElement) {
        return GsonUtil.fromListOrPrimitive(jsonElement, j -> TagKey.create(this.registry, new ResourceLocation(j.getAsString())));
    }

    @Override
    public JsonElement toJSON(List<TagKey<T>> value) {
        if (value.size() == 1) {
            return new JsonPrimitive(value.get(0).location().toString());
        } else {
            JsonArray array = new JsonArray();
            for (TagKey<T> tag : value) {
                array.add(new JsonPrimitive(tag.location().toString()));
            }
            return array;
        }
    }

    @Override
    public List<TagKey<T>> fromNBT(Tag tag, List<TagKey<T>> defaultValue) {
        if (tag instanceof ListTag listTag) {
            List<TagKey<T>> list = new ArrayList<>();

            for (int i = 0; i < listTag.size(); i++) {
                list.add(TagKey.create(this.registry, new ResourceLocation(listTag.getString(i))));
            }

            return list;
        }
        return defaultValue;
    }

    @Override
    public Tag toNBT(List<TagKey<T>> value) {
        ListTag listTag = new ListTag();
        for (TagKey<T> tag : value) {
            listTag.add(StringTag.valueOf(tag.location().toString()));
        }
        return listTag;
    }

    @Override
    public List<TagKey<T>> fromBuffer(FriendlyByteBuf buf) {
        List<TagKey<T>> list = new ArrayList<>();
        int amount = buf.readInt();

        for (int i = 0; i < amount; i++) {
            list.add(TagKey.create(this.registry, buf.readResourceLocation()));
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toBuffer(FriendlyByteBuf buf, Object value) {
        List<TagKey<T>> list = (List<TagKey<T>>) value;
        buf.writeInt(list.size());
        for (TagKey<T> tag : list) {
            buf.writeResourceLocation(tag.location());
        }
    }

    @Override
    public String getString(List<TagKey<T>> value) {
        return value == null ? null : Arrays.toString(value.stream().map(t -> t.location().toString()).toArray());
    }

    @Override
    public String getPropertyType() {
        return "tag_key_list";
    }
}
