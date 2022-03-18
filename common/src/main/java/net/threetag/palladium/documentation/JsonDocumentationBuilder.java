package net.threetag.palladium.documentation;

import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class JsonDocumentationBuilder {

    private String title;
    private String description;
    private final List<Entry<?>> entries = new ArrayList<>();

    public JsonDocumentationBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public JsonDocumentationBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public <T> Entry<T> addProperty(String name, Class<T> clazz) {
        Entry<T> entry = new Entry<T>(name, clazz);
        this.entries.add(entry);
        return entry;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Entry<?>> getEntries() {
        return this.entries;
    }

    public static class Entry<T> {

        private final String name;
        private final Class<T> clazz;
        private String description = null;
        private boolean required = false;
        private T fallbackValue = null;
        private String fallbackValueSerialized = null;
        private JsonElement exampleJson = null;

        private Entry(String name, Class<T> clazz) {
            this.clazz = clazz;
            this.name = name;
        }

        public Entry<T> description(String description) {
            this.description = description;
            return this;
        }

        public Entry<T> required() {
            this.required = true;
            return this;
        }

        public Entry<T> fallback(T value) {
            this.fallbackValue = value;
            this.fallbackValueSerialized = this.fallbackValue == null ? "/" : this.fallbackValue.toString();
            return this;
        }

        public Entry<T> fallback(T value, String serialized) {
            this.fallbackValue = value;
            this.fallbackValueSerialized = serialized;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Entry<T> fallbackObject(Object value) {
            this.fallbackValue = (T) value;
            this.fallbackValueSerialized = this.fallbackValue == null ? null : this.fallbackValue.toString();
            return this;
        }

        @SuppressWarnings("unchecked")
        public Entry<T> fallbackObject(Object value, String serialized) {
            this.fallbackValue = (T) value;
            this.fallbackValueSerialized = serialized;
            return this;
        }

        public Entry<T> exampleJson(JsonElement jsonElement) {
            this.exampleJson = jsonElement;
            return this;
        }

        public String getName() {
            return name;
        }

        public Class<T> getTypeClass() {
            return clazz;
        }

        public String getDescription() {
            return description;
        }

        public boolean isRequired() {
            return required;
        }

        public T getFallbackValue() {
            return fallbackValue;
        }

        public String getFallbackValueSerialized() {
            return this.fallbackValueSerialized;
        }

        public JsonElement getExampleJson() {
            return exampleJson;
        }
    }

}
