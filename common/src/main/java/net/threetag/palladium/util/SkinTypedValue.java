package net.threetag.palladium.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.function.Function;

public class SkinTypedValue<T> {

    private final T normal;
    private final T slim;

    public SkinTypedValue(T value) {
        this.normal = this.slim = value;
    }

    public SkinTypedValue(T normal, T slim) {
        this.normal = normal;
        this.slim = slim;
    }

    public T getNormal() {
        return this.normal;
    }

    public T getSlim() {
        return this.slim;
    }

    public T get(boolean slim) {
        return slim ? this.getSlim() : this.getNormal();
    }

    public T get(LivingEntity livingEntity) {
        return this.get(livingEntity instanceof Player player && PlayerUtil.hasSmallArms(player));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkinTypedValue<?> that)) return false;
        return Objects.equals(normal, that.normal) && Objects.equals(slim, that.slim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(normal, slim);
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
