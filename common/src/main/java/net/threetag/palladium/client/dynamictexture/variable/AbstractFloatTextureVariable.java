package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractFloatTextureVariable implements ITextureVariable {

    private List<Pair<Operation, JsonPrimitive>> operations = new LinkedList<>();

    public AbstractFloatTextureVariable(List<Pair<Operation, JsonPrimitive>> operations) {
        this.operations = operations;
    }

    public AbstractFloatTextureVariable(JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            Operation operation = Operation.getOperationByName(entry.getKey());

            if (operation != null) {
                this.operations.add(Pair.of(operation, entry.getValue().getAsJsonPrimitive()));
            }
        }
    }

    @Override
    public Object get(Entity entity) {
        Number f = this.getNumber(entity);
        for (Pair<Operation, JsonPrimitive> pair : operations) {
            f = pair.getFirst().function.apply(f.floatValue(), pair.getSecond());
        }
        return f;
    }

    public abstract float getNumber(Entity entity);

    public enum Operation {

        ADD("add", (input, addition) -> input + addition),
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
