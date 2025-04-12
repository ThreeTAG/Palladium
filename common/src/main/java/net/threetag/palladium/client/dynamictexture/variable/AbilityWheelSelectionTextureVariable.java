package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

import java.util.List;

public class AbilityWheelSelectionTextureVariable extends AbstractIntegerTextureVariable {

    public AbilityWheelSelectionTextureVariable(List<Pair<Operation, Integer>> operations) {
        super(operations);
    }

    @Override
    public int getNumber(DataContext context) {
        Integer i = context.get(DataContextType.ABILITY_WHEEL_SELECTION);
        return i == null ? 0 : i;
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new AbilityWheelSelectionTextureVariable(AbstractIntegerTextureVariable.parseOperations(json));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the index of the currently selected ability in the ability wheel!";
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Ability Wheel Selection");
            AbstractIntegerTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("ability_wheel_selection");
        }
    }

}
