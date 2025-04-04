package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class AbilityKeyVariable extends StringPathVariable {

    public static final MapCodec<AbilityKeyVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, AbilityKeyVariable::new));

    public AbilityKeyVariable(String molang) {
        super(molang);
    }

    @Override
    public String getString(DataContext context) {
        var ability = context.getAbility();
        return ability != null ? ability.getAbility().getKey() : "";
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.ABILITY_KEY;
    }

    public static class Serializer extends StringSerializer<AbilityKeyVariable> {

        @Override
        public MapCodec<AbilityKeyVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, AbilityKeyVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Key").setDescription("Returns the key of the ability in the context.")
                    .setExampleObject(new AbilityKeyVariable(""));
        }
    }
}
