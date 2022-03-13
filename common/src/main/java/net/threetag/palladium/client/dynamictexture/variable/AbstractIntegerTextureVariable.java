package net.threetag.palladium.client.dynamictexture.variable;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractIntegerTextureVariable implements ITextureVariable {

    private List<Pair<Operation, Integer>> operations = Lists.newLinkedList();

    public AbstractIntegerTextureVariable(List<Pair<Operation, Integer>> operations) {
        this.operations = operations;
    }

    public AbstractIntegerTextureVariable(JsonObject json) {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            Operation operation = Operation.getOperationByName(entry.getKey());

            if (operation != null) {
                this.operations.add(Pair.of(operation, entry.getValue().getAsInt()));
            }
        }
    }

    @Override
    public Object get(LivingEntity entity) {
        int i = this.getNumber(entity);
        for (Pair<Operation, Integer> pair : operations) {
            i = pair.getFirst().function.apply(i, pair.getSecond());
        }
        return i;
    }

    public abstract int getNumber(LivingEntity entity);

    public enum Operation {

        ADD("add", Integer::sum),
        SUBTRACT("subtract", (input, subtract) -> input - subtract),
        MULTIPLY("multiply", (input, multiply) -> input * multiply),
        DIVIDE("divide", (input, multiply) -> input / multiply),
        MIN("min", Integer::max),
        MAX("max", Integer::min),
        MODULO("modulo", (input, modulo) -> input % modulo);

        private final String name;
        private final BiFunction<Integer, Integer, Integer> function;

        Operation(String name, BiFunction<Integer, Integer, Integer> function) {
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
