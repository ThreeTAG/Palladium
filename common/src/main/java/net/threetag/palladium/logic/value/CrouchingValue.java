package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class CrouchingValue extends BooleanValue {

    public static final MapCodec<CrouchingValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            onTrueCodec(), onFalseCodec()
    ).apply(instance, CrouchingValue::new));

    public CrouchingValue(String onTrue, String onFalse) {
        super(onTrue, onFalse);
    }

    @Override
    public boolean getBoolean(DataContext context) {
        var entity = context.getEntity();
        return entity != null && entity.isCrouching();
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.CROUCHING.get();
    }

    public static class Serializer extends BooleanSerializer<CrouchingValue> {

        @Override
        public MapCodec<CrouchingValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, CrouchingValue> builder, HolderLookup.Provider provider) {
            builder.setName("Crouching").setDescription("Checks if the entity is crouching.")
                    .setExampleObject(new CrouchingValue("is_crouching", "not_crouching"));
        }
    }
}
