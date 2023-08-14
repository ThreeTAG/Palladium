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
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FloatPropertyVariable extends AbstractFloatTextureVariable {

    private final String propertyKey;

    public FloatPropertyVariable(String propertyKey, List<Pair<Operation, JsonPrimitive>> operations) {
        super(operations);
        this.propertyKey = propertyKey;
    }

    @Override
    public float getNumber(DataContext context) {
        AtomicReference<Float> result = new AtomicReference<>(0F);
        EntityPropertyHandler.getHandler(context.getEntity()).ifPresent(handler -> {
            PalladiumProperty<?> property = handler.getPropertyByName(this.propertyKey);

            if (property instanceof FloatProperty floatProperty) {
                result.set(handler.get(floatProperty));
            }
        });

        return result.get();
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new FloatPropertyVariable(
                    GsonHelper.getAsString(json, "property"),
                    AbstractFloatTextureVariable.parseOperations(json));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the value of a float property within the player. The math operations can be arranged in any order and are fully optional!";
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Float Property");

            builder.addProperty("property", String.class)
                    .description("Name of the property you want the value from.")
                    .required().exampleJson(new JsonPrimitive("example_property"));

            AbstractFloatTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("float_property");
        }
    }
}
