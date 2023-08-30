package net.threetag.palladium.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.dynamictexture.TextureReference;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.PalladiumRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public abstract class IconSerializer<T extends IIcon> implements IDocumentedConfigurable {

    public static final PalladiumRegistry<IconSerializer> REGISTRY = PalladiumRegistry.create(IconSerializer.class, Palladium.id("icon_serializers"));

    public static IIcon parseJSON(JsonElement json) {
        if (json.isJsonPrimitive()) {
            String input = json.getAsString();

            if (input.endsWith(".png")) {
                return new TexturedIcon(new ResourceLocation(input));
            } else if(input.startsWith("#")) {
                return new TexturedIcon(TextureReference.parse(input));
            } else {
                ResourceLocation id = new ResourceLocation(json.getAsString());

                if (!BuiltInRegistries.ITEM.containsKey(id)) {
                    throw new JsonParseException("Unknown item '" + json.getAsString() + "'");
                }

                return new ItemIcon(BuiltInRegistries.ITEM.get(id));
            }
        } else if (json.isJsonObject()) {
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json.getAsJsonObject(), "type"));

            if (!REGISTRY.containsKey(id)) {
                throw new JsonParseException("Unknown icon type '" + id + "'");
            }

            IconSerializer<?> serializer = REGISTRY.get(id);
            return Objects.requireNonNull(serializer).fromJSON(json.getAsJsonObject());
        } else {
            throw new JsonParseException("Icon must either be a string or an object");
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static JsonElement serializeJSON(IIcon icon) {
        if (icon instanceof ItemIcon itemIcon && itemIcon.stack.getCount() == 1) {
            return new JsonPrimitive(BuiltInRegistries.ITEM.getKey(itemIcon.stack.getItem()).toString());
        } else if (icon instanceof TexturedIcon texturedIcon && texturedIcon.tint == null) {
            return new JsonPrimitive(texturedIcon.texture.toString());
        } else {
            IconSerializer serializer = icon.getSerializer();
            JsonObject json = serializer.toJSON(icon);
            JsonObject json2 = new JsonObject();
            json2.addProperty("type", Objects.requireNonNull(REGISTRY.getKey(serializer)).toString());
            return GsonUtil.merge(json2, json);
        }
    }

    public static IIcon parseNBT(CompoundTag tag) {
        ResourceLocation id = new ResourceLocation(tag.getString("Type"));

        if (!REGISTRY.containsKey(id)) {
            return null;
        }

        IconSerializer<?> serializer = REGISTRY.get(id);
        return Objects.requireNonNull(serializer).fromNBT(tag);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static CompoundTag serializeNBT(IIcon icon) {
        IconSerializer serializer = icon.getSerializer();
        CompoundTag nbt = serializer.toNBT(icon);
        nbt.putString("Type", Objects.requireNonNull(REGISTRY.getKey(serializer)).toString());
        return nbt;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "icons"), "Icons")
                .add(HTMLBuilder.heading("Icons"))
                .addDocumentationSettings(REGISTRY.getValues().stream().sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }

    @NotNull
    public abstract T fromJSON(JsonObject json);

    public abstract T fromNBT(CompoundTag nbt);

    public abstract JsonObject toJSON(T icon);

    public abstract CompoundTag toNBT(T icon);

    @Override
    public ResourceLocation getId() {
        return REGISTRY.getKey(this);
    }
}
