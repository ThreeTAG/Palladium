package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.context.DataContext;

public class SmallArmsTextureVariable extends AbstractBooleanTextureVariable {

    public SmallArmsTextureVariable(String trueValue, String falseValue) {
        super(trueValue, falseValue);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return context.getEntity() instanceof Player player && PlayerUtil.hasSmallArms(player);
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new SmallArmsTextureVariable(
                    AbstractBooleanTextureVariable.parseTrueValue(json),
                    AbstractBooleanTextureVariable.parseFalseValue(json));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns a true/false value depending on whether the player has small arms.";
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Small Arms");

            AbstractBooleanTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("small_arms");
        }
    }

}
