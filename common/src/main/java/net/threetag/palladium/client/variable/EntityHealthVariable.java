package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class EntityHealthVariable extends FloatPathVariable {

    public static final MapCodec<EntityHealthVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, EntityHealthVariable::new));

    public EntityHealthVariable(String molang) {
        super(molang);
    }

    @Override
    public float getFloat(DataContext context) {
        var entity = context.getLivingEntity();
        return entity != null ? entity.getHealth() : 0;
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.ENTITY_HEALTH;
    }

    public static class Serializer extends FloatSerializer<EntityHealthVariable> {

        @Override
        public MapCodec<EntityHealthVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, EntityHealthVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Entity Health").setDescription("Returns the health of the entity.")
                    .setExampleObject(new EntityHealthVariable("value * 2"));
        }
    }
}
