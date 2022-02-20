package net.threetag.palladium.util.icon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.architectury.core.RegistryEntry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class IconSerializer<T extends IIcon> extends RegistryEntry<IconSerializer<T>> {

    public static final ResourceKey<Registry<IconSerializer<?>>> RESOURCE_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Palladium.MOD_ID, "icon_serializers"));
    public static final Registrar<IconSerializer<?>> REGISTRY = Registries.get(Palladium.MOD_ID).builder(RESOURCE_KEY.location(), new IconSerializer<?>[0]).build();

    public static IIcon parseJSON(JsonObject json) {
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "type"));

        if (!REGISTRY.contains(id)) {
            throw new JsonParseException("Unknown icon type '" + id + "'");
        }

        IconSerializer<?> serializer = REGISTRY.get(id);
        return Objects.requireNonNull(serializer).fromJSON(json);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static JsonObject serializeJSON(IIcon icon) {
        IconSerializer serializer = icon.getSerializer();
        JsonObject json = serializer.toJSON(icon);
        json.addProperty("type", Objects.requireNonNull(REGISTRY.getId(serializer)).toString());
        return json;
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

    @NotNull
    public abstract T fromJSON(JsonObject json);

    public abstract T fromNBT(CompoundTag nbt);

    public abstract JsonObject toJSON(T icon);

    public abstract CompoundTag toNBT(T icon);

}
