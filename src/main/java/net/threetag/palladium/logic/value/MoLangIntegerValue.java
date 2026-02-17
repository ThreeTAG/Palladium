package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class MoLangIntegerValue extends IntegerValue {

    public static final MapCodec<MoLangIntegerValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("molang").forGetter(v -> v.molang)
    ).apply(instance, MoLangIntegerValue::new));

    public MoLangIntegerValue(String molang) {
        super(molang);
    }

    @Override
    public int getInteger(DataContext context) {
        return 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.MOLANG_INTEGER.get();
    }

    public static class Serializer extends IntSerializer<MoLangIntegerValue> {

        @Override
        public MapCodec<MoLangIntegerValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, MoLangIntegerValue> builder, HolderLookup.Provider provider) {
            builder.setName("MoLang Integer").setDescription("Allows MoLang usage to calculate an integer")
                    .addExampleObject(new MoLangIntegerValue("query.ground_speed() * 2"));
        }
    }
}
