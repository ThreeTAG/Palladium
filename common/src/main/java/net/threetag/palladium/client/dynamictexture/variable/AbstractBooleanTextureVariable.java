package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;

public abstract class AbstractBooleanTextureVariable implements ITextureVariable {

    public final String trueValue;
    public final String falseValue;

    public AbstractBooleanTextureVariable(String trueValue, String falseValue) {
        this.trueValue = trueValue;
        this.falseValue = falseValue;
    }

    public abstract boolean getBoolean(DataContext context);

    @Override
    public Object get(DataContext context) {
        return this.getBoolean(context) ? this.trueValue : this.falseValue;
    }

    public static String parseTrueValue(JsonObject json) {
        return GsonHelper.getAsString(json, "true_value", "true");
    }

    public static String parseFalseValue(JsonObject json) {
        return GsonHelper.getAsString(json, "false_value", "false");
    }

    public static void addDocumentationFields(JsonDocumentationBuilder builder) {
        builder.addProperty("true_value", Boolean.class)
                .description("If the variable turns out to be true, this value will be inserted into the texture path")
                .fallback(true, "true").exampleJson(new JsonPrimitive("true"));

        builder.addProperty("false_value", Boolean.class)
                .description("If the variable turns out to be false, this value will be inserted into the texture path")
                .fallback(false, "false").exampleJson(new JsonPrimitive("false"));
    }
}
