package net.threetag.palladium.documentation;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public interface IDefaultDocumentedConfigurable extends IDocumentedConfigurable {

    PropertyManager getPropertyManager();

    @Override
    default void generateDocumentation(DocumentationBuilder builder) {
        this.getPropertyManager().values().forEach(new BiConsumer<PalladiumProperty<?>, Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public void accept(PalladiumProperty property, Object val) {
                DocumentationBuilder.Entry<?> entry = builder.addProperty(property.getKey(), val.getClass())
                        .description(property.getDescription())
                        .fallback(val);


                JsonElement v = property.toJSON(val);
                rows.add(Arrays.asList(
                        property.getKey(),
                        property.getType().getTypeName().substring(property.getType().getTypeName().lastIndexOf(".") + 1),
                        property.getDescription(),
                        false,
                        v));
            }
        });
    }

    @Override
    default List<String> getColumns() {
        return Arrays.asList("Setting", "Type", "Description", "Required", "Fallback Value");
    }

    @Override
    default List<Iterable<?>> getRows() {
        List<Iterable<?>> rows = new LinkedList<>();
        this.getPropertyManager().values().forEach(new BiConsumer<PalladiumProperty<?>, Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public void accept(PalladiumProperty property, Object val) {
                JsonElement v = property.toJSON(val);
                rows.add(Arrays.asList(
                        property.getKey(),
                        property.getType().getTypeName().substring(property.getType().getTypeName().lastIndexOf(".") + 1),
                        property.getDescription(),
                        false,
                        v));
            }
        });
        return rows;
    }

    @Override
    default JsonElement getExampleJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.getId().toString());
        this.getPropertyManager().values().forEach(new BiConsumer<PalladiumProperty<?>, Object>() {
            @SuppressWarnings("unchecked")
            @Override
            public void accept(PalladiumProperty property, Object val) {
                JsonElement v = property.toJSON(val);
                json.add(property.getKey(), v);
            }
        });
        return json;
    }
}
