package net.threetag.palladium.util.icon;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import dev.architectury.core.RegistryEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class IconSerializer<T extends IIcon> extends RegistryEntry<IconSerializer<T>> implements IDocumentedConfigurable {

    public static final ResourceKey<Registry<IconSerializer<?>>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "icon_serializers"));
    public static final Registrar<IconSerializer<?>> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new IconSerializer<?>[0]).build();

    public static IIcon parseJSON(JsonElement json) {
        if (json.isJsonPrimitive()) {
            String input = json.getAsString();

            if (input.endsWith(".png")) {
                return new TexturedIcon(new ResourceLocation(input));
            } else {
                ResourceLocation id = new ResourceLocation(json.getAsString());

                if (!Registry.ITEM.containsKey(id)) {
                    throw new JsonParseException("Unknown item '" + json.getAsString() + "'");
                }

                return new ItemIcon(Registry.ITEM.get(id));
            }
        } else if (json.isJsonObject()) {
            ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json.getAsJsonObject(), "type"));

            if (!REGISTRY.contains(id)) {
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
            return new JsonPrimitive(Registry.ITEM.getKey(itemIcon.stack.getItem()).toString());
        } else if (icon instanceof TexturedIcon texturedIcon && texturedIcon.tint == null) {
            return new JsonPrimitive(texturedIcon.texture.toString());
        } else {
            IconSerializer serializer = icon.getSerializer();
            JsonObject json = serializer.toJSON(icon);
            JsonObject json2 = new JsonObject();
            json2.addProperty("type", Objects.requireNonNull(REGISTRY.getId(serializer)).toString());
            return GsonUtil.merge(json2, json);
        }
    }

    public static IIcon parseNBT(CompoundTag tag) {
        ResourceLocation id = new ResourceLocation(tag.getString("Type"));

        if (!REGISTRY.contains(id)) {
            return null;
        }

        IconSerializer<?> serializer = REGISTRY.get(id);
        return Objects.requireNonNull(serializer).fromNBT(tag);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static CompoundTag serializeNBT(IIcon icon) {
        IconSerializer serializer = icon.getSerializer();
        CompoundTag nbt = serializer.toNBT(icon);
        nbt.putString("Type", Objects.requireNonNull(REGISTRY.getId(serializer)).toString());
        return nbt;
    }

    public static HTMLBuilder documentationBuilder() {
        return new HTMLBuilder(new ResourceLocation(Palladium.MOD_ID, "icons"), "Icons")
                .add(HTMLBuilder.heading("Icons"))
                .addDocumentationSettings(REGISTRY.entrySet().stream().map(Map.Entry::getValue).sorted(Comparator.comparing(o -> o.getId().toString())).collect(Collectors.toList()));
    }

    @NotNull
    public abstract T fromJSON(JsonObject json);

    public abstract T fromNBT(CompoundTag nbt);

    public abstract JsonObject toJSON(T icon);

    public abstract CompoundTag toNBT(T icon);

    @Override
    public ResourceLocation getId() {
        return REGISTRY.getId(this);
    }
}
