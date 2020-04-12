package net.threetag.threecore.client.renderer.entity.modellayer.texture.variable;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayerContext;

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
    public Object get(IModelLayerContext context) {
        int i = this.getNumber(context);
        for (Pair<Operation, Integer> pair : operations) {
            i = pair.getFirst().function.apply(i, pair.getSecond());
        }
        return i;
    }

    public abstract int getNumber(IModelLayerContext context);

    public enum Operation {

        ADD("add", Integer::sum),
        SUBTRACT("subtract", (input, subtract) -> input - subtract),
        MULTIPLY("multiply", (input, multiply) -> input * multiply),
        DIVIDE("divide", (input, multiply) -> input / multiply),
        MIN("min", Integer::max),
        MAX("max", Integer::min),
        MODULO("modulo", (input, modulo) -> input % modulo);

        private String name;
        private BiFunction<Integer, Integer, Integer> function;

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
