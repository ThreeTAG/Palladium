package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class EntityTickCountVariable extends IntegerPathVariable {

    public static final MapCodec<EntityTickCountVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, EntityTickCountVariable::new));

    public EntityTickCountVariable(String molang) {
        super(molang);
    }

    @Override
    public int getInteger(DataContext context) {
        var entity = context.getEntity();
        return entity != null ? entity.tickCount : 0;
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.ENTITY_TICK_COUNT;
    }

    public static class Serializer extends IntSerializer<EntityTickCountVariable> {

        @Override
        public MapCodec<EntityTickCountVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, EntityTickCountVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Entity Tick Count").setDescription("Returns the tick count of the entity.")
                    .setExampleObject(new EntityTickCountVariable("value * 2"));
        }
    }
}
