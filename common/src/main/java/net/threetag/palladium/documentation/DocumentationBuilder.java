package net.threetag.palladium.documentation;

import java.util.ArrayList;
import java.util.List;

public class DocumentationBuilder {

    private final List<Entry<?>> entries = new ArrayList<>();

    public <T> Entry<T> addProperty(String name, Class<T> clazz) {
        Entry<T> entry = new Entry<T>(name, clazz);
        this.entries.add(entry);
        return entry;
    }

    public static class Entry<T> {

        private final String name;
        private final Class<T> clazz;
        private String description = null;
        private boolean required = false;
        private T fallbackValue = null;

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
    }

}
