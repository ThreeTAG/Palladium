package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class AbilityKeyValue extends StringValue {

    public static final MapCodec<AbilityKeyValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, AbilityKeyValue::new));

    public AbilityKeyValue(String molang) {
        super(molang);
    }

    @Override
    public String getString(DataContext context) {
        var ability = context.getAbility();
        return ability != null ? ability.getAbility().getKey() : "";
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.ABILITY_KEY.get();
    }

    public static class Serializer extends StringSerializer<AbilityKeyValue> {

        @Override
        public MapCodec<AbilityKeyValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, AbilityKeyValue> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Key").setDescription("Returns the key of the ability in the context.")
                    .addExampleObject(new AbilityKeyValue(""));
        }
    }
}
