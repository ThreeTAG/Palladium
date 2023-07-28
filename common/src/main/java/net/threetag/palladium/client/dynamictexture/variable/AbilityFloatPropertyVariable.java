package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladium.util.property.FloatProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.List;

public class AbilityFloatPropertyVariable extends AbstractFloatTextureVariable {

    private final AbilityReference reference;
    private final String propertyKey;

    public AbilityFloatPropertyVariable(ResourceLocation powerId, String abilityId, String propertyKey, List<Pair<Operation, JsonPrimitive>> operations) {
        super(operations);
        this.reference = new AbilityReference(powerId, abilityId);
        this.propertyKey = propertyKey;
    }

    @Override
    public float getNumber(DataContext context) {
        var livingEntity = context.getLivingEntity();
        if (livingEntity != null) {
            AbilityEntry entry = this.reference.getEntry(livingEntity);

            if (entry == null) {
                return 0F;
            }

            PalladiumProperty<?> property = entry.getEitherPropertyByKey(this.propertyKey);

            if (property instanceof FloatProperty floatProperty) {
                return entry.getProperty(floatProperty);
            }
        }

        return 0F;
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new AbilityFloatPropertyVariable(
                    GsonUtil.getAsResourceLocation(json, "power"),
                    GsonHelper.getAsString(json, "ability"),
                    GsonHelper.getAsString(json, "property"),
                    AbstractFloatTextureVariable.parseOperations(json));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the value of a float property of an ability (e.g. the 'value' of an animation timer). The math operations can be arranged in any order and are fully optional!";
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Ability Float-Property");

            builder.addProperty("power", ResourceLocation.class)
                    .description("ID of the power the ability is in.")
                    .required().exampleJson(new JsonPrimitive("example:power_id"));

            builder.addProperty("ability", String.class)
                    .description("Key of the ability that is being looked for.")
                    .required().exampleJson(new JsonPrimitive("ability_key"));

            builder.addProperty("property", String.class)
                    .description("Name of the property you want the value from. It's 'value' for animation timer ablities.")
                    .required().exampleJson(new JsonPrimitive("value"));

            AbstractFloatTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("ability_float_property");
        }
    }
}
