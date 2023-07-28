package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Mth;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractFloatTextureVariable implements ITextureVariable {

    private final List<Pair<Operation, JsonPrimitive>> operations;

    public AbstractFloatTextureVariable(List<Pair<Operation, JsonPrimitive>> operations) {
        this.operations = operations;
    }

    @Override
    public Object get(DataContext context) {
        Number f = this.getNumber(context);
        for (Pair<Operation, JsonPrimitive> pair : operations) {
            f = pair.getFirst().function.apply(f.floatValue(), pair.getSecond());
        }
        return f;
    }

    public abstract float getNumber(DataContext context);

    public static List<Pair<Operation, JsonPrimitive>> parseOperations(JsonObject json) {
        List<Pair<Operation, JsonPrimitive>> operations = new LinkedList<>();
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            Operation operation = Operation.getOperationByName(entry.getKey());

            if (operation != null) {
                operations.add(Pair.of(operation, entry.getValue().getAsJsonPrimitive()));
            }
        }
        return operations;
    }

    public static void addDocumentationFields(JsonDocumentationBuilder builder) {
        builder.addProperty("add", Float.class)
                .description("This value will be added on top of the returned value.")
                .fallback(null)
                .exampleJson(new JsonPrimitive(5.2F));
        builder.addProperty("subtract", Float.class)
                .description("This value will be subtracted from the returned value.")
                .fallback(null)
                .exampleJson(new JsonPrimitive(3.1F));
        builder.addProperty("multiply", Float.class)
                .description("This value will be subtracted with the returned value.")
                .fallback(null)
                .exampleJson(new JsonPrimitive(2.5F));
        builder.addProperty("divide", Float.class)
                .description("This value will be divided to the returned value.")
                .fallback(null)
                .exampleJson(new JsonPrimitive(7.5F));
        builder.addProperty("min", Float.class)
                .description("Using this value will set a minimum limit for the returned value.")
                .fallback(null)
                .exampleJson(new JsonPrimitive(5.5F));
        builder.addProperty("max", Float.class)
                .description("Using this value will set a maximum limit for the returned value.")
                .fallback(null)
                .exampleJson(new JsonPrimitive(123.4F));
        builder.addProperty("modulo", Float.class)
                .description("Using this value will apply a modulo operation to the returned value.")
                .fallback(null)
                .exampleJson(new JsonPrimitive(5.0));
    }

    public enum Operation {

        ADD("add", Float::sum),
        SUBTRACT("subtract", (input, subtract) -> input - subtract),
        MULTIPLY("multiply", (input, multiply) -> input * multiply),
        DIVIDE("divide", (input, multiply) -> input / multiply),
        MIN("min", Float::max),
        MAX("max", Float::min),
        MODULO("modulo", (input, modulo) -> input % modulo),
        ROUND((input, obj) -> {
            String mode = obj.getAsString();
            if (mode.equalsIgnoreCase("to_int")) {
                return input.intValue();
            } else if (mode.equalsIgnoreCase("ceil")) {
                return Mth.ceil(input);
            } else if (mode.equalsIgnoreCase("floor")) {
                return Mth.floor(input);
            } else {
                return input;
            }
        }, "round");

        private final String name;
        private final BiFunction<Float, JsonPrimitive, Number> function;

        Operation(BiFunction<Float, JsonPrimitive, Number> function, String name) {
            this.name = name;
            this.function = function;
        }

        Operation(String name, BiFunction<Float, Float, Number> function) {
            this.name = name;
            this.function = (input, value) -> {
                if (value.isNumber()) {
                    return function.apply(input, value.getAsFloat());
                } else {
                    return input;
                }
            };
        }

        public static Operation getOperationByName(String name) {
            for (Operation operation : values()) {
                if (operation.name.equalsIgnoreCase(name)) {
                    return operation;
                }
            }
            return null;
        }
    }

}
