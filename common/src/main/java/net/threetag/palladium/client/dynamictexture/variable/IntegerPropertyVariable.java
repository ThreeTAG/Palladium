package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.EntityPropertyHandler;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class IntegerPropertyVariable extends AbstractIntegerTextureVariable {

    private final String propertyKey;
    private final int fallbackValue;

    public IntegerPropertyVariable(String propertyKey, int fallbackValue, List<Pair<Operation, Integer>> operations) {
        super(operations);
        this.propertyKey = propertyKey;
        this.fallbackValue = fallbackValue;
    }

    @Override
    public int getNumber(DataContext context) {
        AtomicInteger result = new AtomicInteger(this.fallbackValue);
        EntityPropertyHandler.getHandler(context.getEntity()).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);

            if (property instanceof IntegerProperty integerProperty) {
                result.set(handler.get(integerProperty));
            }
        });

        return result.get();
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new IntegerPropertyVariable(
                    GsonHelper.getAsString(json, "property"),
                    GsonHelper.getAsInt(json, "fallback", 0),
                    AbstractIntegerTextureVariable.parseOperations(json));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the value of an integer property within the player. The math operations can be arranged in any order and are fully optional!";
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Integer Property");

            builder.addProperty("property", String.class)
                    .description("Name of the property you want the value from.")
                    .required().exampleJson(new JsonPrimitive("example_property"));
            builder.addProperty("fallback", Integer.class)
                    .description("If the property is not found, this value will be used instead.")
                    .exampleJson(new JsonPrimitive(0));

            AbstractIntegerTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("integer_property");
        }
    }
}
