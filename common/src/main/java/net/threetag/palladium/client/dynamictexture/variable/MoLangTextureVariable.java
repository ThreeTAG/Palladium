package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;
import team.unnamed.mocha.MochaEngine;
import team.unnamed.mocha.runtime.compiled.MochaCompiledFunction;
import team.unnamed.mocha.runtime.compiled.Named;

public class MoLangTextureVariable implements ITextureVariable {

    private final MoLangFunction function;

    public MoLangTextureVariable(MoLangFunction function) {
        this.function = function;
    }

    @Override
    public Object get(DataContext context) {
        return this.function.get(context);
    }

    public interface MoLangFunction extends MochaCompiledFunction {

        Object get(@Named("context") DataContext context);

    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            String function = GsonHelper.getAsString(json, "function");
            return new MoLangTextureVariable(MochaEngine.createStandard().compile(function, MoLangFunction.class));
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("MoLang Function");

            builder.addProperty("function", String.class)
                    .description("Allows you to use MoLang to define a texture variable.")
                    .required();
        }

        @Override
        public String getDocumentationDescription() {
            return "Allows you to use MoLang to define a texture variable.";
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("molang");
        }
    }

}
