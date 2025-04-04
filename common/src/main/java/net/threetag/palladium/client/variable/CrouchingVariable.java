package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class CrouchingVariable extends BooleanPathVariable {

    public static final MapCodec<CrouchingVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            onTrueCodec(), onFalseCodec()
    ).apply(instance, CrouchingVariable::new));

    public CrouchingVariable(String onTrue, String onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        var entity = context.getEntity();
        return entity != null && entity.isCrouching();
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.CROUCHING;
    }

    public static class Serializer extends BooleanSerializer<CrouchingVariable> {

        @Override
        public MapCodec<CrouchingVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, CrouchingVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Crouching").setDescription("Checks if the entity is crouching.")
                    .setExampleObject(new CrouchingVariable("is_crouching", "not_crouching"));
        }
    }
}
