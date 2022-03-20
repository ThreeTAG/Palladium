package net.threetag.palladium.documentation;

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
                    .exampleJson(property.toJSON(val));
        });
    }
}
