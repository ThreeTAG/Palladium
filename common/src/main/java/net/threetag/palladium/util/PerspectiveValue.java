package net.threetag.palladium.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;
import java.util.function.Function;

public class PerspectiveValue<T> {

    private final T firstPerson;
    private final T thirdPerson;

    public PerspectiveValue(T value) {
        this.firstPerson = this.thirdPerson = value;
    }

    public PerspectiveValue(T firstPerson, T thirdPerson) {
        this.firstPerson = firstPerson;
        this.thirdPerson = thirdPerson;
    }

    public T getFirstPerson() {
        return this.firstPerson;
    }

    public T getThirdPerson() {
        return this.thirdPerson;
    }

    public T get(boolean firstPerson) {
        return firstPerson ? this.getFirstPerson() : this.getThirdPerson();
    }

    @Environment(EnvType.CLIENT)
    public T get(CameraType cameraType) {
        return this.get(cameraType == CameraType.FIRST_PERSON);
    }

    @Environment(EnvType.CLIENT)
    public T get() {
        return this.get(Minecraft.getInstance().options.getCameraType());
    }

    @Environment(EnvType.CLIENT)
    public T getForPlayer(Player player) {
        if (player == Minecraft.getInstance().player) {
            return this.get();
        } else {
            return this.get(false);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PerspectiveValue<?> that)) return false;
        return Objects.equals(firstPerson, that.firstPerson) && Objects.equals(thirdPerson, that.thirdPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstPerson, thirdPerson);
    }

    @Override
    public String toString() {
        return "PerspectiveValue{" +
                "firstPerson=" + firstPerson +
                ", thirdPerson=" + thirdPerson +
                '}';
    }

    public static <T> PerspectiveValue<T> fromJSON(JsonElement jsonElement, Function<JsonElement, T> parser) {
        if (jsonElement.isJsonObject()) {
            JsonObject json = jsonElement.getAsJsonObject();
            if (GsonHelper.isValidNode(json, "first_person") && GsonHelper.isValidNode(json, "third_person")) {
                return new PerspectiveValue<>(parser.apply(json.get("first_person")), parser.apply(json.get("third_person")));
            } else {
                return new PerspectiveValue<>(parser.apply(jsonElement));
            }
        } else {
            return new PerspectiveValue<>(parser.apply(jsonElement));
        }
    }

    public static <T> PerspectiveValue<T> getFromJson(JsonObject json, String memberName, Function<JsonElement, T> parser, T fallback) {
        if (json.has(memberName)) {
            return fromJSON(json.get(memberName), parser);
        } else {
            return new PerspectiveValue<>(fallback);
        }
    }

    public JsonElement toJson(Function<T, JsonElement> serializer) {
        if (this.firstPerson == this.thirdPerson) {
            return serializer.apply(this.firstPerson);
        } else {
            var json = new JsonObject();
            json.add("first_person", serializer.apply(this.firstPerson));
            json.add("third_person", serializer.apply(this.thirdPerson));
            return json;
        }
    }

}
