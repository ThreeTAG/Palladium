package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractFloatTextureVariable implements ITextureVariable {

    private List<Pair<Operation, Float>> operations = new LinkedList<>();

    public AbstractFloatTextureVariable(List<Pair<Operation, Float>> operations) {
        this.operations = operations;
    }

    public AbstractFloatTextureVariable(JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            Operation operation = Operation.getOperationByName(entry.getKey());

            if (operation != null) {
                this.operations.add(Pair.of(operation, entry.getValue().getAsFloat()));
            }
        }
    }

    @Override
    public Object get(LivingEntity entity) {
        float f = this.getNumber(entity);
        for (Pair<Operation, Float> pair : operations) {
            f = pair.getFirst().function.apply(f, pair.getSecond());
        }
        return f;
    }

    public abstract float getNumber(LivingEntity entity);

    public enum Operation {

        ADD("add", (input, addition) -> input + addition),
        SUBTRACT("subtract", (input, subtract) -> input - subtract),
        MULTIPLY("multiply", (input, multiply) -> input * multiply),
        DIVIDE("divide", (input, multiply) -> input / multiply),
        MIN("min", Float::max),
        MAX("max", Float::min),
        MODULO("modulo", (input, modulo) -> input % modulo);

        private final String name;
        private final BiFunction<Float, Float, Float> function;

        Operation(String name, BiFunction<Float, Float, Float> function) {
            this.name = name;
            this.function = function;
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
