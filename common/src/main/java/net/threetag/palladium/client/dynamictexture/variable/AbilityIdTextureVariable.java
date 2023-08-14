package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;

public class AbilityIdTextureVariable implements ITextureVariable {

    @Override
    public Object get(DataContext context) {
        var ability = context.getAbility();
        return ability != null ? ability.getConfiguration().getId() : "";
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new AbilityIdTextureVariable();
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Ability Id");
        }

        @Override
        public String getDocumentationDescription() {
            return "When used in an ability-context (e.g. ability icons, render layers, gui overlay, skin change), this can return the ID of the ability (not the type!)";
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("ability_id");
        }
    }
}
