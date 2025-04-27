package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.util.concurrent.atomic.AtomicReference;

public class StringPropertyVariable implements ITextureVariable {

    private final String propertyKey;
    private final String fallbackValue;

    public StringPropertyVariable(String propertyKey, String fallbackValue) {
        this.propertyKey = propertyKey;
        this.fallbackValue = fallbackValue;
    }

    @Override
    public Object get(DataContext context) {
        AtomicReference<String> result = new AtomicReference<>(this.fallbackValue);
        EntityPropertyHandler.getHandler(context.getEntity()).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);
            if (property instanceof StringProperty stringProperty) {
                result.set(handler.get(stringProperty));
            }
        });
        return result.get();
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new StringPropertyVariable(GsonHelper.getAsString(json, "property"), GsonHelper.getAsString(json, "fallback", ""));
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("String Property");

            builder.addProperty("property", String.class)
                    .description("Name of the property you want the value from.")
                    .required().exampleJson(new JsonPrimitive("example_property"));
            builder.addProperty("fallback", String.class)
                    .description("If the property is not found, this value will be used instead.")
                    .exampleJson(new JsonPrimitive(""));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the value of a String property within the player.";
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("string_property");
        }
    }
}
