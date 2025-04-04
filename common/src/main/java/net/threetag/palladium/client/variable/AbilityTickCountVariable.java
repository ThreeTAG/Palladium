package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.ability.AbilityReference;

public class AbilityTickCountVariable extends IntegerPathVariable {

    public static final MapCodec<AbilityTickCountVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            AbilityReference.CODEC.fieldOf("ability").forGetter(v -> v.reference),
            modifyFunctionCodec()
    ).apply(instance, AbilityTickCountVariable::new));

    private final AbilityReference reference;

    public AbilityTickCountVariable(AbilityReference reference, String molang) {
        super(molang);
        this.reference = reference;
    }

    @Override
    public int getInteger(DataContext context) {
        if (context.getEntity() instanceof LivingEntity living) {
            var ability = this.reference.getInstance(living, context.getPowerHolder());

            if (ability != null) {
                return ability.getEnabledTicks();
            }
        }

        return 0;
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.ABILITY_TICK_COUNT;
    }

    public static class Serializer extends IntSerializer<AbilityTickCountVariable> {

        @Override
        public MapCodec<AbilityTickCountVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, AbilityTickCountVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Ability Tick Count").setDescription("Returns the ticks the ability in the context has been enabled for.")
                    .add("ability", TYPE_ABILITY_REFERENCE, "The ability to get the ticks from. Defined in this syntax: <power_id>#<ability_key>")
                    .setExampleObject(new AbilityTickCountVariable(AbilityReference.parse("namespace:example_power#ability_key"), ""));
        }
    }
}
