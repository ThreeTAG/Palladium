package net.threetag.palladium.documentation;

import com.google.gson.JsonPrimitive;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.function.BiConsumer;

public interface IDefaultDocumentedConfigurable extends IDocumentedConfigurable {

    PropertyManager getPropertyManager();

    @SuppressWarnings({"rawtypes", "unchecked", "UnstableApiUsage"})
    @Override
    default void generateDocumentation(JsonDocumentationBuilder builder) {
        this.getPropertyManager().values().forEach((BiConsumer<PalladiumProperty, Object>) (property, val) -> {
            builder.addProperty(property.getKey(), property.typeToken.getRawType())
                    .description(property.getDescription())
                    .fallbackObject(property.getString(val))
                    .exampleJson(val != null ? property.toJSON(val) : new JsonPrimitive("null"));
        });
    }
}
