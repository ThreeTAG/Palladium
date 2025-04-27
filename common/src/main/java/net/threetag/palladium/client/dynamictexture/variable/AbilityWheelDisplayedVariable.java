package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class AbilityWheelDisplayedVariable extends AbstractBooleanTextureVariable {

    public AbilityWheelDisplayedVariable(String trueValue, String falseValue) {
        super(trueValue, falseValue);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return context.has(DataContextType.ABILITY_WHEEL_DISPLAYED) && context.get(DataContextType.ABILITY_WHEEL_DISPLAYED);
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new AbilityWheelDisplayedVariable(
                    AbstractBooleanTextureVariable.parseTrueValue(json),
                    AbstractBooleanTextureVariable.parseFalseValue(json));
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Ability Wheel Displayed");
            AbstractBooleanTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("ability_wheel_displayed");
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns a true/false value depending on whether the texture is currently used in an ability, which is shown in the center of an ability wheel.";
        }
    }
}
