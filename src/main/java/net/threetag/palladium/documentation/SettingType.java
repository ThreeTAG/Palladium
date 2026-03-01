package net.threetag.palladium.documentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class SettingType {

    public abstract JsonElement toJson();

    public abstract String toString();

    public static SettingType simple(String name) {
        return new Simple(name);
    }

    public static SettingType enumList(java.lang.Enum<?>[] values) {
        return new Enum(values);
    }

    public static SettingType enumList(Collection<String> values) {
        return new EnumByStrings(values);
    }

    public static SettingType combined(SettingType... options) {
        return new Combined(options);
    }

    public static SettingType list(SettingType type) {
        return simple(type.toString() + "[]");
    }

    public static SettingType listOrPrimitive(SettingType type) {
        return combined(type, simple(type.toString() + "[]"));
    }

    public static SettingType listOrPrimitive(String name) {
        var type = simple(name);
        return combined(type, simple(type + "[]"));
    }

    public static SettingType intRange(int min, int max) {
        return simple("Integer (" + min + " ~ " + max + ")");
    }

    private static class Simple extends SettingType {

        private final String name;

        public Simple(String name) {
            this.name = name;
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(this.name);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public static class Enum extends SettingType {

        private final java.lang.Enum<?>[] values;

        public Enum(java.lang.Enum<?>[] values) {
            this.values = values;
        }

        @Override
        public JsonElement toJson() {
            var json = new JsonObject();
            json.addProperty("type", "enum");

            var array = new JsonArray();
            for (java.lang.Enum<?> value : this.values) {
                array.add(value.name());
            }
            json.add("values", array);

            return json;
        }

        @Override
        public String toString() {
            return Arrays.stream(this.values).map(e -> "\"" + e.name() + "\"").collect(Collectors.joining(" | "));
        }
    }

    public static class EnumByStrings extends SettingType {

        private final Collection<String> values;

        public EnumByStrings(Collection<String> values) {
            this.values = values;
        }

        @Override
        public JsonElement toJson() {
            var json = new JsonObject();
            json.addProperty("type", "enum");

            var array = new JsonArray();
            for (String value : this.values) {
                array.add(value);
            }
            json.add("values", array);

            return json;
        }

        @Override
        public String toString() {
            return this.values.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(" | "));
        }
    }

    public static class Combined extends SettingType {

        private final SettingType[] options;

        public Combined(SettingType... options) {
            this.options = options;
        }

        @Override
        public JsonElement toJson() {
            var json = new JsonObject();
            json.addProperty("type", "combined");

            var array = new JsonArray();
            for (SettingType option : this.options) {
                array.add(option.toJson());
            }
            json.add("options", array);

            return json;
        }

        @Override
        public String toString() {
            return Arrays.stream(this.options).map(SettingType::toString).collect(Collectors.joining(" | "));
        }
    }

}
