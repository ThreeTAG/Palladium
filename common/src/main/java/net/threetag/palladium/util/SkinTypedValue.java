package net.threetag.palladium.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.function.Function;

public record SkinTypedValue<T>(T normal, T slim) {

    public SkinTypedValue(T value) {
        this(value, value);
    }

    public T get(boolean slim) {
        return slim ? this.slim() : this.normal();
    }

    public T get(Entity entity) {
        return this.get(entity instanceof Player player && PlayerUtil.hasSmallArms(player));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkinTypedValue<?> that)) return false;
        return Objects.equals(normal, that.normal) && Objects.equals(slim, that.slim);
    }

    @Override
    public String toString() {
        return "SkinTypedValue{" +
                "normal=" + normal +
                ", slim=" + slim +
                '}';
    }

    public static <T> SkinTypedValue<T> fromJSON(JsonElement jsonElement, Function<JsonElement, T> parser) {
        if (jsonElement.isJsonObject()) {
            JsonObject json = jsonElement.getAsJsonObject();
            if (GsonHelper.isValidNode(json, "normal") && GsonHelper.isValidNode(json, "slim")) {
                return new SkinTypedValue<>(parser.apply(json.get("normal")), parser.apply(json.get("slim")));
            } else {
                return new SkinTypedValue<>(parser.apply(jsonElement));
            }
        } else {
            return new SkinTypedValue<>(parser.apply(jsonElement));
        }
    }

    public JsonElement toJson(Function<T, JsonElement> serializer) {
        if (this.normal == this.slim) {
            return serializer.apply(this.normal);
        } else {
            var json = new JsonObject();
            json.add("normal", serializer.apply(this.normal));
            json.add("slim", serializer.apply(this.slim));
            return json;
        }
    }

}
