package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionEnvironment;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.condition.ConditionSerializers;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;

import java.util.List;

public class ConditionTextureVariable extends AbstractBooleanTextureVariable {

    private final List<Condition> conditions;

    public ConditionTextureVariable(String trueValue, String falseValue, List<Condition> conditions) {
        super(trueValue, falseValue);
        this.conditions = conditions;
    }

    @Override
    public boolean getBoolean(DataContext context) {
        for (Condition condition : this.conditions) {
            if (!condition.active(context)) {
                return false;
            }
        }
        return true;
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new ConditionTextureVariable(
                    AbstractBooleanTextureVariable.parseTrueValue(json),
                    AbstractBooleanTextureVariable.parseFalseValue(json),
                    ConditionSerializer.listFromJSON(json.get("conditions"), ConditionEnvironment.ASSETS));
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Condition");
            
            var example = new JsonArray();
            var crouching = new JsonObject();
            crouching.addProperty("type", ConditionSerializers.CROUCHING.getId().toString());
            example.add(crouching);

            builder.addProperty("conditions", Condition[].class)
                    .description("Array of conditions that will be checked")
                    .required()
                    .exampleJson(example);

            AbstractBooleanTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("condition");
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks a list of conditions (or a single one) and returns a true/false value appropriately.";
        }
    }
}
