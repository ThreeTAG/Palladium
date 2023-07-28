package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;

public class CrouchingTextureVariable extends AbstractBooleanTextureVariable {

    public CrouchingTextureVariable(String trueValue, String falseValue) {
        super(trueValue, falseValue);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        var entity = context.getEntity();
        return entity != null && entity.isCrouching();
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new CrouchingTextureVariable(
                    AbstractBooleanTextureVariable.parseTrueValue(json),
                    AbstractBooleanTextureVariable.parseFalseValue(json));
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Crouching");
            AbstractBooleanTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("crouching");
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns a true/false value depending on whether the player is crouching.";
        }
    }

}
