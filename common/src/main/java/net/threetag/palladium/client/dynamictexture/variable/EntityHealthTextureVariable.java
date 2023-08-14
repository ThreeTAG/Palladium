package net.threetag.palladium.client.dynamictexture.variable;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.util.context.DataContext;

import java.util.List;

public class EntityHealthTextureVariable extends AbstractFloatTextureVariable {

    public EntityHealthTextureVariable(List<Pair<Operation, JsonPrimitive>> operations) {
        super(operations);
    }

    @Override
    public float getNumber(DataContext context) {
        return context.getEntity() instanceof LivingEntity livingEntity ? livingEntity.getHealth() : 0F;
    }

    public static class Serializer implements ITextureVariableSerializer {

        @Override
        public ITextureVariable parse(JsonObject json) {
            return new EntityHealthTextureVariable(AbstractFloatTextureVariable.parseOperations(json));
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns the health of the entity. The math operations can be arranged in any order and are fully optional!";
        }

        @Override
        public void addDocumentationFields(JsonDocumentationBuilder builder) {
            builder.setTitle("Entity Health");
            AbstractFloatTextureVariable.addDocumentationFields(builder);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("entity_health");
        }
    }
}
